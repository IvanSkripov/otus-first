package ru.otus.kotlin.course.app.spring.controllers

//import org.springframework.web.reactive.socket.WebSocketSession

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ru.otus.kotlin.course.api.v1.apiBytesToCreateRequest
import ru.otus.kotlin.course.api.v1.apiMapper
import ru.otus.kotlin.course.api.v1.models.IRequest
import ru.otus.kotlin.course.api.v1.models.ImageCreateRequest
import ru.otus.kotlin.course.app.spring.base.PsSettings
import ru.otus.kotlin.course.app.spring.base.PsWsSession
import ru.otus.kotlin.course.app.spring.base.controllerHelper
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.mappers.fromTransport
import ru.otus.kotlin.course.mappers.toTransport
import java.nio.ByteBuffer

@Component
class PsContollerWS (
    private val appSettings: PsSettings
): WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> {
        val psSession = PsWsSession(session)
        sessions.add(psSession)
        val initMsgs = flow {
            emit( process("ws-init") {
                command = PsCommand.INIT
                wsSession = psSession
            })
        }

        val rcvMsgs = session.receive().asFlow()
            .map { message -> process("ws-handle") {
                wsSession = psSession
                if (message.type == WebSocketMessage.Type.TEXT) {
                    val req = apiMapper.readValue(message.payloadAsText, IRequest::class.java)
                    fromTransport(req)
                } else {
                    val buf = message.payload
                    val bytes = ByteArray(buf.readableByteCount())
                    buf.read(bytes)
                    val cr = apiBytesToCreateRequest(bytes)
                    fromTransport(cr.req, cr.file )
                }
            }
        }

        val outMsgs = merge(initMsgs, rcvMsgs)
            .onCompletion {
                process("ws-finish") {
                    wsSession = psSession
                    command = PsCommand.FINISHED
                    state = PsState.FINISHING
                }
                sessions.remove(psSession)
            }
            .map {
                if (it.command == PsCommand.DOWNLOAD && it.state.isPositive()) {
                    session.binaryMessage{ factory ->
                        factory.wrap(ByteBuffer.wrap(it.response.file))
                    }
                } else {
                    val m = it.toTransport()
                    session.textMessage(apiMapper.writeValueAsString(m))
                }
            }
            .asFlux()

        return session.send(outMsgs)
    }

    private suspend fun process(logId: String, block: PsBeContext.() -> Unit) = appSettings.controllerHelper(
       getRequest = block,
       logId = logId,
       clazz = this@PsContollerWS::class
   )
}
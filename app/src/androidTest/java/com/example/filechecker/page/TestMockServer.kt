package com.example.filechecker.page

import com.example.filechecker.mock.EventData
import com.example.filechecker.logger.L
import com.example.filechecker.mock.ServerResponsesData
import com.example.filechecker.mock.SimpleDispatcher
import com.example.filechecker.qa.TestApp.TAG
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

import java.net.BindException


class TestMockServer {

    private val mockWebServer = MockWebServer()

    /**
     * Start mock web server with local host ip: 127.0.0.1
     * port: 8080
     * Url = "http://127.0.0.1:8080"
     */
    fun startMockServer() {
        val port = 8080
        try {
            mockWebServer.start(port)
            L.i(TAG, "========== Mock Server: START ============================")
        } catch (e: BindException) {
            // If the server is not restarted there will be problems with responses
            L.i(TAG, "Mock server was started, make new test run")
        }
    }


    /**
     *  Get device request and set server response
     * @param serverResponses: server responses
     */
    fun getRequestAndSetResponse(serverResponses: ServerResponsesData) {
        val request: RecordedRequest = mockWebServer.takeRequest()
        val simpleDispatcher = SimpleDispatcher(serverResponses = serverResponses, recordedRequest = request, rtpData = null)
        val response = simpleDispatcher.dispatch(request)
        mockWebServer.enqueue(response)
    }

    /**
     * Get device request and set server result response and check detection data
     * @param rtpData: detection data
     */
    fun getResultRequestAndSetResponse(serverResponses: ServerResponsesData, rtpData: EventData) {
        val request: RecordedRequest = mockWebServer.takeRequest()
        val simpleDispatcher = SimpleDispatcher(serverResponses = serverResponses, recordedRequest = request, rtpData = rtpData)
        val response = simpleDispatcher.dispatch(request)
        mockWebServer.enqueue(response)
    }

    /**
     * Shutdown mock web server
     */
    fun shutDownMockServer() {
        L.i("MockTest", "========== Mock Server: SHUTDOWN =========================")
        mockWebServer.shutdown()
    }
}
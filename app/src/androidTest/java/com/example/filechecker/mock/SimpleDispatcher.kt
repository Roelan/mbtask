package com.example.filechecker.mock

import com.example.filechecker.utils.ForAssetFiles
import com.example.filechecker.logger.L
import com.example.filechecker.qa.TestApp.TAG
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class SimpleDispatcher(
    private val serverResponses: ServerResponsesData,
    recordedRequest: RecordedRequest,
    rtpData: EventData?
) : Dispatcher() {

    private val testMethod = ForAssetFiles()
    private val parseRequestFromDevice = ParseResultFromDevice(recordedRequest, rtpData)

    @Override
    override fun dispatch(request: RecordedRequest): MockResponse {

        when (request.path.toString()) {
            "/api/v1/machine" -> {
                val myJson: String = testMethod.getJson(serverResponses.machineJson)
                parseRequestFromDevice.parseRequestFromDevice()
                L.i(TAG, "========== Response from server: AUTH ====================")
                L.i(TAG, "Set JSON: authDevResponse.json")
                return MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("cache-control", "no-store")
                    .addHeader("Vary", "Accept-Encoding")
                    .addHeader("x-content-type-options", "nosniff")
                    .addHeader("x-frame-options", "DENY")
                    .addHeader("x-xss-protection", "1; mode=block")
                    .addHeader("x-cache", "Miss from cloudfront")
                    .addHeader("x-amz-cf-pop", "IAD89-C1")
                    .addHeader("x-amz-cf-id", "_thuWLG7hPLabxAXfVmCpLXM5cn_uW1hduLeGUi5b22eTJY9_R4zHA==")
                    .setBody(myJson)
            }
            "/api/v1/machine/results" -> {
                parseRequestFromDevice.parseResultFromDevice()
                L.i(TAG, "========== Response from server: 200 =====================")
                L.i("MockTest", "Empty body")
                return MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/x-gzip")
                    .addHeader("cache-control", "no-store")
                    .addHeader("Vary", "Accept-Encoding")
                    .addHeader("x-content-type-options", "nosniff")
                    .addHeader("x-frame-options", "DENY")
                    .addHeader("x-xss-protection", "1; mode=block")
                    .addHeader("x-cache", "Miss from cloudfront")
                    .addHeader("x-amz-cf-pop", "IAD89-C1")
                    .addHeader("x-amz-cf-id", "py2E5ukXu0jVs4Fyz3q4exx1OJr5K6TiES4JyQYOkuXqFXu9ooCIzw")
            }
            "/api/v1/machine/sync" -> {
                parseRequestFromDevice.parseRequestFromDevice()
                L.i(TAG, "========== Response from server: SYNC ====================")
                L.i(TAG, "Set JSON: ${serverResponses.syncJson}")
                val myJson: String = testMethod.getJson(serverResponses.syncJson)
                return MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/x-gzip")
                    .addHeader("cache-control", "no-store")
                    .addHeader("Vary", "Accept-Encoding")
                    .addHeader("x-content-type-options", "nosniff")
                    .addHeader("x-frame-options", "DENY")
                    .addHeader("x-xss-protection", "1; mode=block")
                    .addHeader("x-cache", "Miss from cloudfront")
                    .addHeader("x-via", "1.1 936f33bed45438343f0ef2adff442815.cloudfront.net (CloudFront)")
                    .addHeader("x-amz-cf-pop", "AD89-C1")
                    .addHeader("x-amz-cf-id", "EeuoXIAmIbso0bbT2GCl1aVLhWyyWbKfBlMOwMRn1jfGlDuqbw9_mA")
                    .setBody(myJson)
            }
            "/api/v1/machine/policy" -> {
                parseRequestFromDevice.parseRequestFromDevice()
                L.i(TAG, "========== Response from server: POLICY ==================")
                L.i(TAG, "Set JSON: ${serverResponses.policyJson}")
                val myJson: String = testMethod.getJson(serverResponses.policyJson)
                return MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/x-gzip")
                    .addHeader("cache-control", "no-store")
                    .addHeader("Vary", "Accept-Encoding")
                    .addHeader("x-content-type-options", "nosniff")
                    .addHeader("x-frame-options", "DENY")
                    .addHeader("x-xss-protection", "1; mode=block")
                    .addHeader("x-cache", "Miss from cloudfront")
                    .addHeader("x-amz-cf-pop", "IAD89-C1")
                    .addHeader("x-amz-cf-id", "mmT55l5zjQ2xJ7zrUL6FQm0smoTfwlwgUnX448XwjCDVLfho2g01vw==")
                    .setBody(myJson)
            }
            "/api/v1/machine/schedule" -> {
                parseRequestFromDevice.parseRequestFromDevice()
                L.i(TAG, "========== Response from server: SCHEDULE ================")
                L.i(TAG, "Set JSON: ${serverResponses.scheduleJson}")
                val myJson: String = testMethod.getJson(serverResponses.scheduleJson)
                return MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/x-gzip")
                    .addHeader("cache-control", "no-store")
                    .addHeader("Vary", "Accept-Encoding")
                    .addHeader("x-content-type-options", "nosniff")
                    .addHeader("x-frame-options", "DENY")
                    .addHeader("x-xss-protection", "1; mode=block")
                    .addHeader("x-cache", "Miss from cloudfront")
                    .addHeader("x-amz-cf-pop", "IAD89-C1")
                    .addHeader("x-amz-cf-id", "FNm6vYYgJj8zloQtT2zxBUWiXlUo5MDY1MUOgR5K65mqJ7nYMEq-6w")
                    .setBody(myJson)
            }
            else -> {
                L.i("MockTest", "========== Response from server: 400 =====================")
                return MockResponse().setResponseCode(400)
            }
        }
    }
}
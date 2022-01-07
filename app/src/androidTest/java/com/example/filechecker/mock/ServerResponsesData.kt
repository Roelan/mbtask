package com.example.filechecker.mock

data class ServerResponsesData(
    var syncJson: String = "syncResponse.json",
    var policyJson: String = "policyUpdate.json",
    var machineJson: String = "authDevResponse.json",
    var scheduleJson: String = "scheduleResponse.json")
﻿// Please see documentation at https://docs.microsoft.com/aspnet/core/client-side/bundling-and-minification
// for details on configuring this project to bundle and minify static web assets.

// Write your JavaScript code.
"use strict";
var con = new signalR.HubConnectionBuilder().withUrl("/hub").build();
con.on("LoadAll", function () { location.reload(); });
con.start().then().catch(function (err) {
    return console.log(err.toString());
});
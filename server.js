/*
* @Author: Manraj Singh
* @Date:   2016-08-19 20:20:19
* @Last Modified by:   Manraj Singh
* @Last Modified time: 2016-08-19 21:22:26
*/

'use strict';
const express = require('express');
const app = express();
const http = require('http').Server(app);
const io = require('socket.io')(http);

app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

http.listen(3000, function(){
  console.log('listening on *:3000');
});

io.on('connection', function(socket) {
  socket.emit('announcements', { message: 'A new user has joined!' });
  socket.on('event', function(data) {
    socket.emit('mapData', { message: 'A new user has joined!' });
  });
});

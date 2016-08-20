/*
* @Author: Manraj Singh
* @Date:   2016-08-19 20:20:19
* @Last Modified by:   Manraj Singh
* @Last Modified time: 2016-08-20 01:04:26
*/

var WebSocketServer = require('websocket').server;
var http = require('http');

function Socketer (SocketerPort, callback) {
  var PORT = SocketerPort || 3000;

  var server = http.createServer(function (request, response) {
    console.log((new Date()) + ' Received request for ' + request.url);
    response.writeHead(404);
    response.end();
  });

  server.listen(PORT, function () {
    console.log((new Date()) + ' Server is listening on port ' + PORT)
  });

  var wsServer = new WebSocketServer({
    httpServer: server,
    autoAcceptConnections: false
  });

  function originIsAllowed (origin) {
    return true;
  }

  wsServer.on('request', function (request) {

    if (!originIsAllowed(request.origin)) {
      // Make sure we only accept requests from an allowed origin
      request.reject();
      console.log((new Date()) + ' Connection from origin ' + request.origin + ' rejected.');
      return;
    }

    var connection = request.accept('arduino', request.origin);
    console.log((new Date()) + ' Connection accepted.');

    connection.on('message', function (message) {
      console.log('DATA ', message);
      if (message.type === 'utf8') {
        console.log('Received Message: ' + message.utf8Data)
        callback(message.utf8Data);
        //connection.sendUTF(message.utf8Data)
      } else if (message.type === 'binary') {
        console.log('Received Binary Message of ' + message.binaryData.length + ' bytes');
        connection.sendBytes(message.binaryData);
      }
    });

    connection.on('close', function (reasonCode, description) {
      console.log((new Date()) + ' Peer ' + connection.remoteAddress + ' disconnected.');
    });

    connection.sendUTF('Hallo Client!');
  })

  return wsServer;
}
module.exports = Socketer;

/*
* @Author: Manraj Singh
* @Date:   2016-08-19 20:20:19
* @Last Modified by:   Manraj Singh
* @Last Modified time: 2016-08-20 10:40:06
*/

var express = require('express');
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var Socketer = require('./lib/Socketer');
var mysql      = require('mysql');

var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : 'root',
  database : '200OK'
});

connection.connect();

Socketer(3000, onEmittedData);

function onEmittedData(newAction) {
  console.log('Action: ', newAction);
  if(newAction.indexOf('LAT') == -1 || newAction.indexOf('LON') == -1 || newAction.indexOf('ax') == -1 || newAction.indexOf('ay') == -1 || newAction.indexOf('az') == -1 ){
    return;
  }
  newAction = newAction.replace("\r\n", "").replace("\r","").replace("\n","");
  var res = newAction.split(' ');
  var final = {};
  if(res.length < 5){
    return;
  }

  for(var i=0;i<res.length;i++){
    console.log(res[i]);
    var temp = res[i].split('=');
    final[temp[0]] = temp[1];
  }
  console.log(final);
  var timestamp = Math.round(new Date().getTime()/1000);
  var hid = 7, gpsLatitude = final['LAT'], gpsLongitude = final['LON'], accX = final['ax'], accY = final['ay'], accZ = final['az'];

  connection.query('SELECT * FROM updated WHERE hardwareID='+hid+'', function(err, rows) {
    console.log('Hello! ');
    if (err){
      console.log('Err: here!');
      throw err;
    }
    else{
      console.log(rows);
      if(rows.length == 0){
        console.log('INSERTING!');
        connection.query('INSERT INTO updated VALUES('+hid+','+timestamp+','+gpsLatitude+','+gpsLongitude+','+accX+','+accY+','+accZ+')', function(err, rows){
          if (err){
            throw err;
          }
          console.log('Data Inserted');
        });
      }
      else{
        console.log('UPDATING!');
        connection.query('UPDATE updated SET timeStamp='+timestamp+',gpsLatitude='+gpsLatitude+',gpsLongitude='+gpsLongitude+',accX='+accX+',accY='+accY+',accZ='+accZ+' WHERE hardwareID='+hid+'', function(err, rows){
          if (err){
            throw err;
          }
          console.log('Data UPDATED');
        });
      }
    }
  });
  connection.query('INSERT INTO logger(hardwareID, timeStamp, gpsLatitude, gpsLongitude, accX, accY, accZ) VALUES('+hid+','+timestamp+','+gpsLatitude+','+gpsLongitude+', '+accX+','+accY+','+accZ+')', function(err, rows){
    if (err){
      throw err;
    }
    console.log('Data inserted in logger');
  });
}

var PORT = process.env.PORT || 8000;
var pub = __dirname + '/static';
app.use(express.static(pub));

/*app.get('/activity', function (req, res) {
  /*PeopleAction.find({})
    .limit(100)
    .sort('-created')
    .select('action created')
    .exec(function (error, peopleActions) {
      if (error) {
        console.log('\n\nError on read people action: ', error)
      }
      res.json(peopleActions)
    });
});*/

io.on('connection', function (socket) {
  console.log('Socket.io Ok');
});

http.listen(PORT, function serverOn () {
  console.log('http://localhost:' + PORT);
});


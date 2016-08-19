/*
* @Author: Manraj Singh
* @Date:   2016-08-19 20:20:19
* @Last Modified by:   Manraj Singh
* @Last Modified time: 2016-08-19 23:45:02
*/

var mongoose = require('mongoose')
var Schema = mongoose.Schema

var counterSchema = new Schema({
  action: {
    type: String,
    enum: ['in', 'out']
  },
  created: {
    type: Date,
    default: Date.now
  }
})

var Counter = mongoose.model('User', counterSchema)

module.exports = Counter

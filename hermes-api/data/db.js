const mode = process.env.NODE_ENV || 'dev'
const config = require(`../config/${mode}.json`)[mode]
const mongoClient = require('mongodb').MongoClient
let _db

const dbUtils = {
  connect () {
    return new Promise((resolve, reject) => {
      mongoClient.connect(`mongodb://${config.database.host}:${config.database.port}`, { useUnifiedTopology: true })
        .then(con => {
          _db = con.db('hermes')
          resolve()
        })
        .catch(reject)
    })
  },
  getConnection () {
    return _db
  }
}

module.exports = dbUtils

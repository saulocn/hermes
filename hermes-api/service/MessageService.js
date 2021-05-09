const db = require('../data/db')
const producer = require('../pulsar/MessageProducer')

const COLLECTION_NAME = 'sample'

function sendMessage (message) {
    producer.sendMessage(message)
}

module.exports = { sendMessage }

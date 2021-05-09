const db = require('../data/db')
const producer = require('../pulsar/MessageProducer')
const COLLECTION_NAME = 'messages'

const sendMessage = async (message) => {
    await db.getConnection().collection(COLLECTION_NAME).insertOne(message)
    producer.sendMessage(message)
}

module.exports = { sendMessage }

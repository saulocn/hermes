const mode = process.env.NODE_ENV || 'dev'
const config = require(`../config/${mode}.json`)[mode]
const client = require('./PulsarClient').client

const consumer = async () => {
    await client.subscribe({
        topic: config.pulsar.topic,
        subscription: config.pulsar.consumer,
        subscriptionType: 'Exclusive',
        listener: (msg, msgConsumer) => {
            console.log(`message received: ${msg.getData().toString()}`)
            msgConsumer.acknowledge(msg)
        },
    })
}

module.exports = { consumer }


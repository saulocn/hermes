const client = require('./PulsarClient').client
const TOPIC_NAME = 'message-topic'


const consumer = async () => {
    await client.subscribe({
        topic: TOPIC_NAME,
        subscription: 'nodejs-hermes-sub',
        subscriptionType: 'Exclusive',
        listener: (msg, msgConsumer) => {
            console.log(`message received: ${msg.getData().toString()}`)
            msgConsumer.acknowledge(msg)
        },
    })
}


module.exports = { consumer }


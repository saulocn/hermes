const router = require('express').Router()
const service = require('../../service/MessageService')

module.exports = (app) => {
  router
    .route('/')
    .post(async (req, res) => {
      let c
      try {
        console.log(req.body)
        c = await service.sendMessage(req.body)
      } catch (err) {
        console.error('erro', err)
      }
      res.send(c)
    }
    )
  app.use('/messages', router)
}

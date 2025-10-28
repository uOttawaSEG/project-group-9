"use strict";

const {setGlobalOptions} = require("firebase-functions/v2");
const {onDocumentCreated} = require("firebase-functions/v2/firestore");
const {defineSecret, defineString} = require("firebase-functions/params");
const logger = require("firebase-functions/logger");
const sgMail = require("@sendgrid/mail");

setGlobalOptions({maxInstances: 10});


const SENDGRID_API_KEY = defineSecret("SENDGRID_API_KEY");

const SENDGRID_TEMPLATE_ID = defineString("SENDGRID_TEMPLATE_ID");
const DEFAULT_FROM = defineString("DEFAULT_FROM");

exports.sendMailOnCreate = onDocumentCreated(
    {document: "mail/{docId}", secrets: [SENDGRID_API_KEY]},
    async (event) => {
      try {
        const snap = event.data;
        if (!snap) {
          logger.warn("No event.data in Firestore trigger.");
          return;
        }

        const data = snap.data();
        if (!data) {
          logger.warn("No document data in Firestore trigger.");
          return;
        }

        const to = data.to;
        const from = data.from || DEFAULT_FROM.value();
        const templateData = data.templateData || {};

        if (!to) {
          logger.warn("Missing \"to\" field in mail document.");
          return;
        }

        sgMail.setApiKey(SENDGRID_API_KEY.value());

        const msg = {
          to,
          from,
          templateId: SENDGRID_TEMPLATE_ID.value(),
          dynamicTemplateData: templateData,
        };

        await sgMail.send(msg);
        logger.info("Email sent via SendGrid.", {to});
      } catch (err) {
      // Do not throw to avoid infinite retries on bad input
        logger.error("Failed to send email via SendGrid", err);
      }
    },
);

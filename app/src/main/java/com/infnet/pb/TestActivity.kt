package com.infnet.pb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import javax.mail.*
import javax.mail.internet.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        main()
    }

    fun main() {
        val userName =  "lokaiextreme@gmail.com"
        val password =  "hnzymrwyoqmexrsm"
        // FYI: passwords as a command arguments isn't safe
        // They go into your bash/zsh history and are visible when running ps

        val emailFrom = "lokaiextreme@gmail.com"
        val emailTo = "igor.ssouza@al.infnet.edu.br"
        val emailCC = "test"

        val subject = "SMTP Test"
        val text = "Hello Kotlin Mail"

        val props = Properties()
        putIfMissing(props, "mail.smtp.host", "smtp.office365.com")
        putIfMissing(props, "mail.smtp.port", "587")
        putIfMissing(props, "mail.smtp.auth", "true")
        putIfMissing(props, "mail.smtp.starttls.enable", "true")

        val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(userName, password)
            }
        })

        session.debug = true

        try {
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(emailFrom))
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false))
            mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailCC, false))
            mimeMessage.setText(text)
            mimeMessage.subject = subject
            mimeMessage.sentDate = Date()

            val smtpTransport = session.getTransport("smtp")
            smtpTransport.connect()
            smtpTransport.sendMessage(mimeMessage, mimeMessage.allRecipients)
            smtpTransport.close()
        } catch (messagingException: MessagingException) {
            messagingException.printStackTrace()
        }
    }

    private fun putIfMissing(props: Properties, key: String, value: String) {
        if (!props.containsKey(key)) {
            props[key] = value
        }
    }

}
package gsm.gsmnetindo.firebasecode

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class verificationphone: AppCompatActivity() {

    lateinit var verificationId: String
    lateinit var mAuth: FirebaseAuth
    lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificationphone)

        mAuth = FirebaseAuth.getInstance()

        editText = findViewById(R.id.code_verifikasi)

        val phonenmber = intent.getStringExtra("phoneNo")
        sendVerificationcode(phonenmber)

        findViewById<Button>(R.id.verification).setOnClickListener(View.OnClickListener {
            val code = editText.text.toString()
            if (code.isEmpty() || code.length < 6) {
                editText.error = "Enter code"
                editText.requestFocus()
                return@OnClickListener
            }
            verificationcode(code)
        })

    }

    private fun verificationcode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signincredential(credential)
    }

    private fun signincredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //                            Intent intent = new Intent(verificationActivity.this, MainActivity.class);
                    //                            startActivity(intent);
                    Toast.makeText(
                        this@verificationphone,
                        "Your Account has been created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                    //Perform Your required action here to either let the user sign In or do something required
                    val intent =
                        Intent(applicationContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@verificationphone,
                        task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun sendVerificationcode(phonenmber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonenmber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks)
    }

    private val callbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                s: String,
                forceResendingToken: ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                Toast.makeText(this@verificationphone, "code sudah terkirim", Toast.LENGTH_SHORT)
                    .show()
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Toast.makeText(this@verificationphone, "login berhasil", Toast.LENGTH_SHORT)
                    .show()
                val code = phoneAuthCredential.smsCode
                if (code != null){
                    verificationcode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(
                    this@verificationphone,
                    "pengiriman kode gagal",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}
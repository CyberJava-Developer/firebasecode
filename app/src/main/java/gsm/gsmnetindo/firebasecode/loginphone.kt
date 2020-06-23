package gsm.gsmnetindo.firebasecode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class loginphone: AppCompatActivity() {

    lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginphone)

        editText = findViewById(R.id.numberphone)

        //set send code verification
        findViewById<Button>(R.id.login).setOnClickListener(View.OnClickListener {
            val nmber = editText.text.toString()
            if (nmber.isEmpty() || nmber.length <= 11) {
                editText.error = "format nomor telepon salah"
                editText.error = "Contoh +62829...."
                editText.requestFocus()
                return@OnClickListener
            }

            //Call the next activity and pass phone no with it
            val intent =
                Intent(applicationContext, verificationphone::class.java)
            intent.putExtra("phoneNo", nmber)
            startActivity(intent)
        })

    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null) {
            //sudah terdaftar
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}
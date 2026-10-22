
package com.mahdi.cyberlab

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.Toast

class MainActivity : Activity() {

    private val bg = Color.rgb(12, 16, 25)
    private val panel = Color.rgb(24, 31, 45)
    private val accent = Color.rgb(0, 220, 170)
    private val white = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 28, 22, 20)
            setBackgroundColor(bg)
        }

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val title = TextView(this).apply {
            text = "CYBER LAB PRO"
            textSize = 27f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }
        content.addView(title, matchWrap())

        val subtitle = TextView(this).apply {
            text = "ANDROID SECURITY TOOLKIT"
            textSize = 12f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 24)
        }
        content.addView(subtitle, matchWrap())

        addSection(content, "أدوات Termux")

        addTool(
            content,
            "فتح Termux",
            "فتح الطرفية لتنفيذ الأوامر يدويًا"
        ) {
            launchTermux(null)
        }

        addTool(
            content,
            "تثبيت Nmap",
            "إنشاء أمر التثبيت وفتحه في Termux"
        ) {
            launchTermux("pkg update && pkg install nmap")
        }

        addTool(
            content,
            "فحص جهازك المحلي",
            "يعرض أمر فحص أساسي يمكنك مراجعته قبل تشغيله"
        ) {
            launchTermux("nmap -sT localhost")
        }

        addTool(
            content,
            "معلومات Nmap",
            "عرض إصدار Nmap المثبت"
        ) {
            launchTermux("nmap --version")
        }

        addSection(content, "أدوات التحليل")

        addTool(
            content,
            "SHA-256",
            "فتح الطرفية لحساب بصمة ملف"
        ) {
            launchTermux(
                "sha256sum /sdcard/Download/اسم-الملف"
            )
        }

        addTool(
            content,
            "تحديث الحزم",
            "تحديث فهارس الحزم في Termux"
        ) {
            launchTermux("pkg update")
        }

        val note = TextView(this).apply {
            text = """
                ملاحظات:
                • ثبّت Termux من مصدره الرسمي.
                • يجب تثبيت Nmap داخل Termux أولًا.
                • التطبيق يفتح الطرفية؛ لا ينفّذ الأوامر بصمت.
                • راجع كل أمر قبل تشغيله.
                
                Powered by Mahdi
            """.trimIndent()
            textSize = 13f
            setTextColor(Color.LTGRAY)
            setPadding(4, 24, 4, 16)
        }
        content.addView(note, matchWrap())

        scroll.addView(content)
        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }

    private fun addSection(
        parent: LinearLayout,
        name: String
    ) {
        val heading = TextView(this).apply {
            text = name
            textSize = 19f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(0, 18, 0, 10)
        }
        parent.addView(heading, matchWrap())
    }

    private fun addTool(
        parent: LinearLayout,
        name: String,
        description: String,
        action: () -> Unit
    ) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 12, 16, 14)
            setBackgroundColor(panel)
        }

        val button = Button(this).apply {
            text = name
            textSize = 16f
            setTextColor(white)
            setBackgroundColor(Color.rgb(30, 80, 82))
            isAllCaps = false
            setOnClickListener { action() }
        }

        val detail = TextView(this).apply {
            text = description
            textSize = 13f
            setTextColor(Color.LTGRAY)
            setPadding(4, 8, 4, 0)
        }

        card.addView(button, matchWrap())
        card.addView(detail, matchWrap())

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = 12
        parent.addView(card, params)
    }

    private fun launchTermux(command: String?) {
        val intent = Intent(
            "com.termux.RUN_COMMAND"
        ).apply {
            setClassName(
                "com.termux",
                "com.termux.app.RunCommandService"
            )
            putExtra(
                "com.termux.RUN_COMMAND_PATH",
                "/data/data/com.termux/files/usr/bin/bash"
            )
            putExtra(
                "com.termux.RUN_COMMAND_ARGUMENTS",
                arrayOf("-lc", command ?: "echo 'Termux ready'")
            )
            putExtra(
                "com.termux.RUN_COMMAND_WORKDIR",
                "/data/data/com.termux/files/home"
            )
            putExtra(
                "com.termux.RUN_COMMAND_BACKGROUND",
                false
            )
        }

        try {
            startService(intent)
            Toast.makeText(
                this,
                "تم إرسال الطلب إلى Termux",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "تعذّر فتح Termux. تأكد من تثبيته وإعداد صلاحية RUN_COMMAND.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun matchWrap(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}

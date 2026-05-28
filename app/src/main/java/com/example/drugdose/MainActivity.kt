package com.example.drugdose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.drugdose.ui.theme.DrugDoseTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Firebase.firestore
        
        setContent {
            var outputText by remember { mutableStateOf("Press a button to interact with Firestore...") }

            DrugDoseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Button 1: Read and Print
                        Button(onClick = {
                            outputText = "Fetching drugs..."
                            db.collection("drugs").get()
                                .addOnSuccessListener { result ->
                                    if (result.isEmpty) {
                                        outputText = "The 'drugs' collection is empty."
                                    } else {
                                        val sb = StringBuilder()
                                        for (document in result) {
                                            val data = document.data
                                            sb.append("ID: ${document.id}\nData: $data\n\n")
                                        }
                                        outputText = sb.toString()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    outputText = "Error fetching data: ${e.message}"
                                }
                        }) {
                            Text("Print All Drugs")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Button 2: Write from JSON (Disabled)
                        Button(
                            onClick = {
                                outputText = "Uploading data from drugs.json to Firestore..."
                                seedDatabase { resultMsg ->
                                    outputText = resultMsg
                                }
                            },
                            enabled = false
                        ) {
                            Text("Update Firestore from JSON")
                        }

                        Text(
                            text = outputText,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }

    private fun seedDatabase(onResult: (String) -> Unit) {
        val db = Firebase.firestore
        try {
            val jsonString = assets.open("drugs.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val drugsArray = jsonObject.getJSONArray("drugs")

            val total = drugsArray.length()
            var processed = 0
            var successCount = 0

            for (i in 0 until total) {
                val drugJson = drugsArray.getJSONObject(i)
                val id = drugJson.getString("id")
                val drugMap = jsonObjectToMap(drugJson)

                db.collection("drugs").document(id).set(drugMap)
                    .addOnCompleteListener { task ->
                        processed++
                        if (task.isSuccessful) successCount++
                        
                        if (processed == total) {
                            onResult("Upload complete: $successCount/$total drugs updated.")
                        }
                    }
            }
        } catch (e: Exception) {
            onResult("Upload failed: ${e.message}")
            Log.e("Seeding", "Error", e)
        }
    }

    private fun jsonObjectToMap(obj: JSONObject): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        val keys = obj.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = obj.get(key)
            map[key] = when (value) {
                is JSONObject -> jsonObjectToMap(value)
                is JSONArray -> jsonArrayToList(value)
                JSONObject.NULL -> null
                else -> value
            }
        }
        return map
    }

    private fun jsonArrayToList(arr: JSONArray): List<Any?> {
        val list = mutableListOf<Any?>()
        for (i in 0 until arr.length()) {
            val value = arr.get(i)
            list.add(when (value) {
                is JSONObject -> jsonObjectToMap(value)
                is JSONArray -> jsonArrayToList(value)
                JSONObject.NULL -> null
                else -> value
            })
        }
        return list
    }
}

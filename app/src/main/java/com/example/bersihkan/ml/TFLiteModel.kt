package com.example.bersihkan.ml

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class TFLiteModel(private val context: Context) {

    private lateinit var interpreter: Interpreter

    // Function to load the TFLite model
    fun loadModel() {
        try {
            val model = loadModelFile(FILE_NAME)
            interpreter = Interpreter(model)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Function to read the model file from assets and return ByteBuffer
    private fun loadModelFile(modelFilename: String): ByteBuffer {
        val fileDescriptor = context.assets.openFd(modelFilename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // Function to perform inference using the loaded model
    fun doInference(inputData: FloatArray): FloatArray {
        val outputArray = FloatArray(NUM_CLASSES) // Adjust NUM_CLASSES as per your model output size

        try {
            // Prepare the input map
            val inputs: MutableMap<Int, Any> = HashMap()
            inputs[0] = inputData // Assuming the input tensor index is 0

            // Run inference
            val outputs: MutableMap<Int, Any> = HashMap()
            outputs[0] = outputArray // Assuming the output tensor index is 0
            interpreter.runForMultipleInputsOutputs(arrayOf(inputs), outputs)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace() // Print the stack trace for debugging
        }

        return outputArray
    }


    companion object {
        private const val FILE_NAME = "recommendation_model.tflite"
        private const val NUM_CLASSES = 5 // Change this according to your model's output
    }

}
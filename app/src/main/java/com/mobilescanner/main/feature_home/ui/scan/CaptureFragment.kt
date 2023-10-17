package com.mobilescanner.main.feature_home.ui.scan

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.hardware.display.DisplayManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.concurrent.futures.await
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_project.data.entity.toImageItem
import com.mobilescanner.main.main.data.utils.FileUtils
import com.mobilescanner.main.main.data.utils.FileUtils.logD
import com.mobilescanner.main.main.data.utils.FileUtils.logE
import com.mobilescanner.main.main.data.utils.FileUtils.toFile
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CaptureFragment : Fragment() {

    companion object {
        fun newInstance() = CaptureFragment()
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private lateinit var viewModel: ScanViewModel
    private lateinit var mainView:View
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private lateinit var camera: Camera
    private var lensFacing:Int = CameraSelector.LENS_FACING_BACK
    private lateinit var cameraProvider:ProcessCameraProvider
    private val displayManager by lazy { requireActivity().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_capture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        viewModel = ViewModelProvider(requireActivity())[ScanViewModel::class.java]
        cameraExecutor = Executors.newSingleThreadExecutor()
        initPermission()
        initButton()
    }
    private fun initPermission() {
        PermissionX.init(this)
            .permissions(
                REQUIRED_PERMISSIONS.toList()
            )
            .request { allGranted, _, _ ->
                if ( allGranted ) prepareCamera()
                else{
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        REQUIRED_PERMISSIONS,
                        REQUEST_CODE_PERMISSIONS
                    )
                }
            }
    }
    private fun prepareCamera(){
        val viewFinder = mainView.findViewById<PreviewView>(R.id.viewFinder)
        viewFinder.post {
            lifecycleScope.launch {
                cameraProvider = ProcessCameraProvider
                    .getInstance( requireActivity() )
                    .await()
                bindCamera()
            }
        }
    }
    private fun bindCamera() {
        val viewFinder = mainView.findViewById<PreviewView>(R.id.viewFinder)
        val rotation = viewFinder.display.rotation
        val cameraSelector = CameraSelector
            .Builder()
            .requireLensFacing(lensFacing).
            build()
        val preview = Preview.Builder()
            .setTargetRotation(rotation)
            .build()
        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setTargetRotation(rotation)
            .build()
        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()
        try {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture)
            preview.setSurfaceProvider(viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e("Capture", "Use case binding failed", exc)
        }
    }
    private fun takePhoto(){
        val imageCapture = imageCapture ?: return
        val photoFile = FileUtils.getOutputFile(requireActivity(),"移动扫描王-${FileUtils.getCurrentTime()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor( requireActivity() ), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    "Photo capture failed: ${exc.message}".logE()
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    viewModel.imageItem = photoFile.toImageItem()
                    findNavController().navigate(R.id.action_captureFragment_to_editFragment)
                }
            }
        )
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK ){
            it.data?.data?.let { uri ->
                val filePath = FileUtils.getFilePathFromUri( requireActivity() ,uri )
                "Selected $filePath".logD("Capture")
                filePath?.let {
                    viewModel.imageItem = it.toFile().toImageItem()
                    findNavController().navigate(R.id.action_captureFragment_to_editFragment)
                }
            }
        }
    }
    private fun initButton() {
        val capture = mainView.findViewById<ImageView>(R.id.capture)
        capture.setOnClickListener { takePhoto() }
        val flipCamera = mainView.findViewById<ImageView>(R.id.flipCamera)
        flipCamera.setOnClickListener {
            lensFacing = lensFacing xor 1
            bindCamera()
        }
        val importImage = mainView.findViewById<ImageView>(R.id.importImage)
        importImage.setOnClickListener { FileUtils.callAlbum(launcher) }
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
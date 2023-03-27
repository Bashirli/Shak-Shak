package com.bashirli.saksak.ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AlertDialogLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bashirli.saksak.R
import com.bashirli.saksak.adapter.CommentAdapter
import com.bashirli.saksak.databinding.ActivityProductBinding
import com.bashirli.saksak.databinding.AddCommentBinding
import com.bashirli.saksak.model.CommentModel
import com.bashirli.saksak.model.CommentResultModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.util.CustomProgressBar
import com.bashirli.saksak.viewmodel.main.ProductMVVM
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding
    private lateinit var viewModel: ProductMVVM
    private var isFav: Boolean? = null
    private lateinit var mainProductModel: ProductModel
    private var adapter = CommentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ProductMVVM::class.java)
        setup()

    }


    private fun setup() {
        binding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 0) {
                binding.imageLoad.y = scrollY.toFloat()
            } else {
                binding.imageLoad.y = 0f
            }
        }

        binding.cardView.setOnClickListener {
            finish()
        }

        val imageUrl = intent.getStringExtra("imageURL")
        println(imageUrl)
        val id = intent.getIntExtra("productId", 0)

        val arrayList = ArrayList<SlideModel>()
        arrayList.add(SlideModel(imageUrl))
        binding.imageLoad.setImageList(arrayList, ScaleTypes.FIT)


        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle(R.string.loading)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Please Wait")

        CoroutineScope(Dispatchers.Main).launch {
            delay(200)
            progressDialog.show()
            viewModel.loadData(id)
            observeData(progressDialog)
        }

        with(binding) {
            addFav.setOnClickListener {
                favoriteAction()
            }


            recyclerComment.layoutManager = LinearLayoutManager(this@ProductActivity)
            recyclerComment.adapter = adapter

            addComment.setOnClickListener {
             if(viewModel.isLoged()){
                 val layout = AddCommentBinding.inflate(layoutInflater)
                 val alert = MaterialAlertDialogBuilder(this@ProductActivity)

                 alert.setView(layout.root).setCancelable(false).setTitle(R.string.addCom)
                     .setNegativeButton(R.string.cancel) { dialog, which ->
                         return@setNegativeButton
                     }
                 val dialog=alert.create()
                 dialog.show()
                 layout.postButton.setOnClickListener {
                     if (layout.header.text!!.trim().toString().isEmpty() ||
                         layout.mainText.text!!.trim().toString().isEmpty() ||
                         layout.ratingBar.rating == 0f
                     ) {
                         Toast.makeText(
                             this@ProductActivity,
                             R.string.fillTheGaps,
                             Toast.LENGTH_LONG
                         ).show()
                         return@setOnClickListener
                     } else {
                        viewModel.uploadComment(mainProductModel.id,
                            layout.header.text.toString(),layout.mainText.text.toString(),
                            layout.ratingBar.rating.toString())
                         viewModel.uploadCommentData.observe(this@ProductActivity){
                             val progress=CustomProgressBar(this@ProductActivity)
                             progress.startBar()
                             it?.let {data->
                                 when(data.isSuccess){
                                     true->{
                                         progress.stopBar()
                                         Toast.makeText(this@ProductActivity,R.string.sucPost,Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()
                                     }
                                     false->{
                                         progress.stopBar()
                                         Toast.makeText(this@ProductActivity,data.message,Toast.LENGTH_SHORT).show()
                                     }
                                 }
                             }
                         }
                     }
                 }
             }else{
                 Toast.makeText(this@ProductActivity,R.string.signintocon,Toast.LENGTH_SHORT).show()
                 return@setOnClickListener
             }
            }


                commentReviews.setOnClickListener {
                    if (commentSection.visibility == View.GONE) {
                        //comments opened
                        setupComments(it)
                        commentSection.visibility = View.VISIBLE
                        commentIco.setImageResource(R.drawable.openarrow)
                    } else {
                        commentSection.visibility = View.GONE
                        commentIco.setImageResource(R.drawable.closearrow)
                    }
                }


        }
    }

        private fun setupComments(view: View) {
            viewModel.loadComments(mainProductModel.id)
            viewModel.commentData.observe(this@ProductActivity) {
                with(binding) {
                    progressIndicator.visibility = View.VISIBLE

                    it?.let {
                        progressIndicator.visibility = View.GONE
                        when (it.status) {
                            true -> {
                                adapter.updateList(it.data!!)
                            }
                            false -> {

                                Snackbar.make(view, it.message, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.ok) {
                                        return@setAction
                                    }.show()
                            }
                        }

                    }
                }

            }

        }


        private fun favoriteAction() {

            isFav?.let {
                if (it) {
                    viewModel.deleteFav()
                    binding.imageOfFav.setImageResource(R.drawable.heartico)
                } else {
                    viewModel.insertFav()
                    binding.imageOfFav.setImageResource(R.drawable.heartselectedico)
                }
                isFav = !it
            } ?: Toast.makeText(this@ProductActivity, R.string.error, Toast.LENGTH_SHORT).show()
        }

        private fun setData(productModel: ProductModel) {
            val imageList = ArrayList<SlideModel>()
            for (data in productModel.images) {
                imageList.add(SlideModel(data))
            }
            mainProductModel = productModel
            binding.imageLoad.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
            binding.priceOfItem.setText("₼${productModel.price}")
            binding.productTitle.setText(productModel.title)
            binding.productDetail.setText(productModel.description)
            binding.productInfo.setText(productModel.description)
            binding.brandText.setText(productModel.title)
            isFav = viewModel.checkIsOnFav
            isFav?.let {
                if (it) {
                    binding.imageOfFav.setImageResource(R.drawable.heartselectedico)
                } else {
                    binding.imageOfFav.setImageResource(R.drawable.heartico)
                }
            } ?: Toast.makeText(this@ProductActivity, R.string.error, Toast.LENGTH_SHORT).show()

        }

        private fun observeData(progressDialog: ProgressDialog) {
            viewModel.success.observe(this) {
                if (it) {
                    viewModel.productData.observe(this) { data ->
                        setData(data)
                        progressDialog.cancel()
                    }
                }
            }

            viewModel.errorData.observe(this) {
                if (it) {
                    progressDialog.cancel()
                    val alertDialog = AlertDialog.Builder(this@ProductActivity)
                    alertDialog.setTitle(R.string.attention)
                        .setMessage(viewModel.errorMessage)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok) { dialog, which ->
                            finish()
                        }.create().show()
                }
            }
            viewModel.loading.observe(this) {
                if (it) {
                    progressDialog.show()
                }
            }
        }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    }



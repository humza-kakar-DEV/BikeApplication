package com.example.smartbikefyp

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.bumptech.glide.Glide
import com.example.smartbikefyp.databinding.ItemInfoAlertDialogLayoutBinding
import com.example.smartbikefyp.databinding.NavMenuAlertDialogBinding
import com.example.smartbikefyp.databinding.NavMenuCloseApplicationAlertDialogBinding
import com.example.smartbikefyp.databinding.NavMenuScrollAlertDialogBinding
import com.example.smartbikefyp.databinding.PurchaseAlertDialogBinding
import com.example.smartbikefyp.fragment.global.MainActivity
import com.example.smartbikefyp.model.ItemBoughtProduct
import com.example.smartbikefyp.model.ItemBoughtService
import com.example.smartbikefyp.model.UpdateDrawerInfo
import com.example.smartbikefyp.util.BoughtDataType
import com.example.smartbikefyp.util.NavAlertDialogType
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.StringTokenizer


fun Context.log(data: String, key: String = "hm123") {
    Log.d(key, "${data}")
}

fun Context.toast(data: String) {
    Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
}

fun BottomNavigationView.changeMenuLayout(menuRes: Int) {
    this.menu.clear()
    this.inflateMenu(menuRes)
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(this.windowToken, 0);
}

fun Context.getMimeType(uri: Uri): String? {
    val extension: String?

    //Check uri format to avoid null
    extension = if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
        //If scheme is a content
        val mime = MimeTypeMap.getSingleton()
        mime.getExtensionFromMimeType(this.getContentResolver().getType(uri))
    } else {
        //If scheme is a File
        //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
        MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.getPath())).toString())
    }
    return extension
}

fun Context.convertTo12Hr(time24Hr: String): String? {
    try {
        val sdf = SimpleDateFormat("H:mm")
        val dateObj: Date = sdf.parse(time24Hr)
        return SimpleDateFormat("K:mm").format(dateObj)
    } catch (e: ParseException) {
        e.printStackTrace()
        return e.message
    }
}

fun Context.checkPermission(permissionName: String): Boolean =
    this.checkSelfPermission(permissionName) == PackageManager.PERMISSION_GRANTED

internal fun Context.alterText(description: String): String {
    var numWords: Int = 0
    val token = StringTokenizer(description, " ")
    var word: String = token.nextToken()
    numWords = token.countTokens()
//    numWords variable counts words in a paragraph and returns length
    numWords++
    if (numWords > 10) {
        var stringBuilder = StringBuilder()
        description.split(" ").forEachIndexed { index, s ->
            if (index <= 9) {
                stringBuilder.append("${s} ")
            }
        }
        stringBuilder.append("••••")
        return stringBuilder.toString()
    } else {
        return description
    }
}

fun String.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
        // we are using this flag to give a consistent behaviour
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

//  USED REIFIED FUNCTION TO KNOW THE TYPE OF CLASS OF GENERIC T IN THE PARAMETER
inline fun <reified T> Activity.itemInfoAlertDialog(itemBought: T, boughtDataType: BoughtDataType) {
    val builder = AlertDialog.Builder(this).create()
    val binding = ItemInfoAlertDialogLayoutBinding.inflate(this.layoutInflater)
    binding.apply {
        when (T::class) {
            ItemBoughtProduct::class -> {
                (itemBought as ItemBoughtProduct).apply {
                    when (boughtDataType) {
                        BoughtDataType.USER -> {
                            shopAddressContainer.visibility = View.VISIBLE
                            Glide
                                .with(this@itemInfoAlertDialog)
                                .load(itemBought.creatorUser?.imageUri)
                                .centerCrop()
                                .into(imageFilterView)
                            textview18.text = itemBought.creatorUser?.userId
                            textview16.text = itemBought.creatorUser?.phoneNumber
                            textview17.text = itemBought.creatorUser?.yourAddress
                            textview20.text = itemBought.product?.productId
                            textview21.text = itemBought.product?.productCategory
                        }

                        BoughtDataType.MECHANIC -> {
                            shopAddressContainer.visibility = View.GONE
                            Glide
                                .with(this@itemInfoAlertDialog)
                                .load(itemBought.buyerUser?.imageUri)
                                .centerCrop()
                                .into(imageFilterView)
                            textview18.text = itemBought.buyerUser?.userId
                            textview16.text = itemBought.buyerUser?.phoneNumber
                            textview17.text = itemBought.buyerUser?.yourAddress
                            textview20.text = itemBought.product?.productId
                            textview21.text = itemBought.product?.productCategory
                        }
                    }
                }
            }

            ItemBoughtService::class -> {
                (itemBought as ItemBoughtService).apply {
                    when (boughtDataType) {
                        BoughtDataType.USER -> {
                            shopAddressContainer.visibility = View.VISIBLE
                            Glide
                                .with(this@itemInfoAlertDialog)
                                .load(itemBought.creatorUser?.imageUri)
                                .centerCrop()
                                .into(imageFilterView)
                            textview18.text = itemBought.creatorUser?.userId
                            textview16.text = itemBought.creatorUser?.phoneNumber
                            textview17.text = itemBought.creatorUser?.yourAddress
                            textview20.text = itemBought.service?.serviceId
                            textview21.text = itemBought.service?.serviceCategory
                        }

                        BoughtDataType.MECHANIC -> {
                            shopAddressContainer.visibility = View.GONE
                            Glide
                                .with(this@itemInfoAlertDialog)
                                .load(itemBought.buyerUser?.imageUri)
                                .centerCrop()
                                .into(imageFilterView)
                            textview18.text = itemBought.buyerUser?.userId
                            textview16.text = itemBought.buyerUser?.phoneNumber
                            textview17.text = itemBought.buyerUser?.yourAddress
                            textview20.text = itemBought.service?.serviceId
                            textview21.text = itemBought.service?.serviceCategory
                        }
                    }
                }
            }
        }
        okButton.setOnClickListener {
            builder.cancel()
        }
    }
    builder.setView(binding.root)
    builder.setCancelable(false)
    builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    builder.show()
}

fun Activity.navAlertDialog(
    heading: Int,
    description: Int,
    navAlertDialogType: NavAlertDialogType
) {
    val builder = AlertDialog.Builder(this).create()
    when (navAlertDialogType) {
        NavAlertDialogType.SIMPLE_ALERT_DIALOG -> {
            val binding = NavMenuAlertDialogBinding.inflate(this.layoutInflater)
            binding.textView3.text = getString(heading)
            binding.textView7.text = getString(description)
            binding.okButton.setOnClickListener {
                builder.cancel()
            }

            builder.setView(binding.root)
            builder.setCancelable(false)
            builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            builder.show()
        }

        NavAlertDialogType.SCROLL_ALERT_DIALOG -> {
            val binding = NavMenuScrollAlertDialogBinding.inflate(this.layoutInflater)
            binding.textView3.text = getString(heading)
            binding.textView7.text = getString(description)
            binding.okButton.setOnClickListener {
                builder.cancel()
            }

            builder.setView(binding.root)
            builder.setCancelable(false)
            builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            builder.show()
        }
    }
}

object ExtensionFunction {
    fun navAlertDialogCloseApplication(activity: Activity) {
        AlertDialog.Builder(activity).create().apply {
            val binding = NavMenuCloseApplicationAlertDialogBinding.inflate(this.layoutInflater)
            binding.cancelButton.setOnClickListener {
                cancel()
            }
            binding.exitButton.setOnClickListener {
                cancel()
                activity.finish()
            }

            setView(binding.root)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (isShowing) {
                activity.toast("alert dialog showing")
            }
            show()
        }
    }
}

fun Activity.purchaseAlertDialog() {
    AlertDialog.Builder(this).create().apply {
        val binding = PurchaseAlertDialogBinding.inflate(this.layoutInflater)
        binding.okButton.setOnClickListener {
            cancel()
        }

        setView(binding.root)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        show()
    }
}

fun Activity.navAlertDialogCloseApplication(callback: () -> Unit) {
    AlertDialog.Builder(this).create().apply {
        val binding = NavMenuCloseApplicationAlertDialogBinding.inflate(this.layoutInflater)
        binding.cancelButton.setOnClickListener {
            cancel()
        }
        binding.exitButton.setOnClickListener {
            cancel()
            callback()
            this@navAlertDialogCloseApplication.finish()
        }

        setView(binding.root)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (isShowing) {
//            this@navAlertDialogCloseApplication.toast("alert dialog showing")
            return@apply
        }
        show()
    }
}

fun NavigationView.updateDrawerNavigation(
    context: Context,
    updateDrawerInfo: UpdateDrawerInfo
) {
    val drawerHeader = this.getHeaderView(0)
    val drawerHeaderNameTextView = drawerHeader.findViewById<TextView>(R.id.drawerNameTextView)
    val drawerHeaderEmailTextView = drawerHeader.findViewById<TextView>(R.id.drawerEmailTextView)
    val drawerHeaderImageView = drawerHeader.findViewById<ImageFilterView>(R.id.drawerImageView)

    updateDrawerInfo.apply {
        Glide
            .with(context)
            .load(if (imageUri == null) context.packageManager.getApplicationIcon(context.packageName) else imageUri!!)
            .centerCrop()
            .into(drawerHeaderImageView)
        drawerHeaderNameTextView.text = title
        drawerHeaderEmailTextView.text = description
    }

}

internal fun String.isValidPassword(): Boolean {
    this.let { password ->
        if (password.length < 8) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }
                .firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }
                .firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false
    }

    return true
}



























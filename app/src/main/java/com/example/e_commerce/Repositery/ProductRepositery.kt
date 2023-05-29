package com.example.e_commerce.Repositery

import androidx.core.net.toUri
import com.example.e_commerce.Model.Cartitem
import com.example.e_commerce.Model.Order
import com.example.e_commerce.Model.Product
import com.example.e_commerce.Utility.Utility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*

class ProductRepositery {

    private var mauth: FirebaseAuth
    private var mstorage: StorageReference
    private var db: CollectionReference
    private var  favdb :CollectionReference
    private var  carddb :CollectionReference
    private var orderdb:CollectionReference

    init {

        mauth = Firebase.auth
        mstorage = Firebase.storage.reference
        db = Firebase.firestore.collection("Product")
        favdb = Firebase.firestore.collection("Fav")
        carddb = Firebase.firestore.collection("cart")
        orderdb = Firebase.firestore.collection("order")

    }



    fun submitData(product: Product, onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        val reference = mstorage.child("productImages").child(Date().time.toString())

        reference.putFile(product.imageUrl.toUri()).addOnCompleteListener {

            if (it.isSuccessful) {


                reference.downloadUrl.addOnSuccessListener { imageUri ->



                    db.document(product.id).set(product.copy(imageUrl = imageUri.toString()))
                        .addOnCompleteListener {

                            onSuccess()

                        }.addOnFailureListener {


                            onFailure(it.toString())
                        }


                }

            }

        }

    }


    //Order

    fun addtoOrder(order: Order){

        orderdb.document(order.id).set(order)
            .addOnSuccessListener {

            }.addOnFailureListener {


            }

    }


    fun removeToordrdb(order: Order){

        orderdb.document(order.id).delete().addOnSuccessListener {

        }.addOnFailureListener {

        }
    }


    //CardProduct

    fun addTocart(cartitem: Cartitem){

        carddb.document(cartitem.id).set(cartitem)
            .addOnSuccessListener {

            }.addOnFailureListener {


            }

    }


    fun removeTocart(cartitem: Cartitem){

        carddb.document(cartitem.id).delete().addOnSuccessListener {

        }.addOnFailureListener {

        }
    }




    //Favourite
    fun addFav(product: Product){


        favdb.document(product.id).set(product)
            .addOnSuccessListener {

            }.addOnFailureListener {


            }

    }


    fun removeFav(product: Product){

        favdb.document(product.id).delete().addOnSuccessListener {

        }.addOnFailureListener {

        }

    }


}
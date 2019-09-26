package com.rickh.movieapp.util

import android.content.Context
import android.os.Build
import android.util.FloatProperty
import android.util.Property
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator


class AnimUtils {

    /**
     * A delegate for creating a [Property] of `float` type.
     */
    abstract class FloatProp<T> protected constructor(val name: String) {

        abstract operator fun set(`object`: T, value: Float)
        abstract operator fun get(`object`: T): Float
    }

    companion object {
        private var fastOutSlowIn: Interpolator? = null

        fun getFastOutSlowInInterpolator(context: Context): Interpolator? {
            if (fastOutSlowIn == null) {
                fastOutSlowIn = AnimationUtils.loadInterpolator(
                    context,
                    android.R.interpolator.fast_out_slow_in
                )
            }
            return fastOutSlowIn
        }

        /**
         * The animation framework has an optimization for `Properties` of type
         * `float` but it was only made public in API24, so wrap the impl in our own type
         * and conditionally create the appropriate type, delegating the implementation.
         */
        fun <T> createFloatProperty(impl: FloatProp<T>): Property<T, Float> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                object : FloatProperty<T>(impl.name) {
                    override fun get(`object`: T): Float? {
                        return impl[`object`]
                    }

                    override fun setValue(`object`: T, value: Float) {
                        impl[`object`] = value
                    }
                }
            } else {
                object : Property<T, Float>(Float::class.java, impl.name) {
                    override fun get(`object`: T): Float? {
                        return impl[`object`]
                    }

                    override fun set(`object`: T, value: Float?) {
                        impl[`object`] = value!!
                    }
                }
            }
        }
    }
}
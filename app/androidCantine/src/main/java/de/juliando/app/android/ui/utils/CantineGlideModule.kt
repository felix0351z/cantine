package de.juliando.app.android.ui.utils

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Priority
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import de.juliando.app.data.LocalDataStore
import de.juliando.app.repository.ContentRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.ByteBuffer

@GlideModule
class CantineGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL) // Save all images in the cache
        builder.setDefaultRequestOptions(requestOptions)

        super.applyOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(String::class.java, ByteBuffer::class.java, KtorModelLoaderFactory())
    }

    class KtorModelLoaderFactory : ModelLoaderFactory<String, ByteBuffer> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, ByteBuffer> = KtorModelLoader()

        override fun teardown() {}

    }


    class KtorModelLoader : ModelLoader<String, ByteBuffer>, KoinComponent {

        private val TAG = "GlideModule-KtorModelLoader"

        override fun buildLoadData(model: String, width: Int, height: Int, options: Options): ModelLoader.LoadData<ByteBuffer> {
            Log.d(TAG, "Load model with the id $model")
            // Set unique identifier to hold the data in cache
            val diskCacheKey = ObjectKey(model)
            val repo by inject<ContentRepository>()
            val fetcher = KtorDataFetcher(model, repo)

            return ModelLoader.LoadData(diskCacheKey, fetcher)
        }

        /*
        * If an url starts with the address of the cantine server,
        * the access must be handled by the ktor client
        *
        */
        override fun handles(model: String): Boolean {
            return model.startsWith(LocalDataStore.url)
        }


    }

    class KtorDataFetcher(private val model: String, private val contentRepository: ContentRepository) : DataFetcher<ByteBuffer> {

        private val TAG = "GlideModule-KtorDataFetcher"

        override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in ByteBuffer>) {

            // Load the image via ktor. If the coroutine will be cancelled the return value will be a failed data callback
            return try {
                Log.d(TAG, "Try to fetch model with the id $model")
                runBlocking {
                    val bytes = contentRepository.loadPicture(model)
                    val buffer = ByteBuffer.wrap(bytes)
                    callback.onDataReady(buffer)
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error occurred while fetching the model $model", ex)
                callback.onLoadFailed(ex)
            }

        }

        override fun cleanup() {} // Empty because there is nothing to clean up.

        //TODO: Try to handle a possible timeout. Also needed in the ServerDataSource class.
        override fun cancel() {} // Not implemented now, because of the conflicts with coroutines and the java async pattern

        // Specify the response value
        override fun getDataClass(): Class<ByteBuffer> {
            return ByteBuffer::class.java
        }

        // Data source from which the image will be loaded
        override fun getDataSource(): DataSource {
            return DataSource.REMOTE
        }


    }

}
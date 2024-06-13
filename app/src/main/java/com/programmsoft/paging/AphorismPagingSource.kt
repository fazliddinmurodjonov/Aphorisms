package com.programmsoft.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.programmsoft.models.AphorismItemResponse
import com.programmsoft.repository.Repository
import com.programmsoft.utils.Functions
import retrofit2.HttpException

class AphorismPagingSource(private val repository: Repository) :
    PagingSource<Int, AphorismItemResponse>() {
    override fun getRefreshKey(state: PagingState<Int, AphorismItemResponse>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AphorismItemResponse> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getAphorismsList(currentPage)
            val data = response.body()
            val responseData = mutableListOf<AphorismItemResponse>()
            data!!.data.forEach { data ->
                data.isNew = 1
                responseData.add(data)
                Functions.insertData(data)
            }

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}
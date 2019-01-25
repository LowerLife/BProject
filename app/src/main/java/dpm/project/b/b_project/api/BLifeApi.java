package dpm.project.b.b_project.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BLifeApi {

    @GET("/items")
    Call<List<ItemData>> getItem(@Query("price")int price, @Query("size")int size);

}

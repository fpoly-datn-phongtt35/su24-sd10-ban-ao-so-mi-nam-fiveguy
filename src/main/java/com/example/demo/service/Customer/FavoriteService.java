package com.example.demo.service.Customer;

import com.example.demo.entity.Favorite;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FavoriteService {
    List<Favorite> getAllFavorite();

//    Page<Favorite> getAll(Integer page);

    Favorite getFavoriteById(Long id);

    Page<Favorite> getAllFavoritePage(Integer page);

    Favorite createFavorite(Favorite favoriteEntity);

    Favorite updateFavorite(Favorite favoriteEntity, Long id);

    void deleteFavorite(Long id);
// //OL

//  //  List<OlFavoritesResponse> getFavoriteListByUsername(String username);

//    void deleteFavorite(Long id);

//    Integer addFavorite(FavoritesAddResponse addressRequest);

//    Page<FavoritesResponse> findAllByCustomer(Long customerId, int page, int size);
//  //END OL
}

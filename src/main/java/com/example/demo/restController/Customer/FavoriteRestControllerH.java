package com.example.demo.restController.Customer;

import com.example.demo.entity.Favorite;
import com.example.demo.service.Customer.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/favorite")
@CrossOrigin("*")

public class FavoriteRestControllerH {
    @Autowired
    FavoriteService favoriteService;

    @GetMapping("/favorite")
    public ResponseEntity<List<Favorite>> getAllAddress() {
        List<Favorite> favorite = favoriteService.getAllFavorite();
        return ResponseEntity.ok(favorite);
    }

//    @GetMapping("/pageall")
//    public ResponseEntity<Page<Favorite>> getAllFavoritePage(@RequestParam(defaultValue = "0", name = "page") Integer page) {
//        return ResponseEntity.ok(favoriteService.getAllFavoritePage(page));
//    }
//
//    @GetMapping("/findby/{favoriteId}")
//    public ResponseEntity<Favorite> getFavoriteById(@PathVariable Long favoriteId) {
//        Favorite favorite = favoriteService.getFavoriteById(favoriteId);
//        if (favorite != null) {
//            return ResponseEntity.ok(favorite);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PostMapping("")
    public ResponseEntity<?> createFavorite(@RequestBody Favorite favoriteEntity) {
        try {
            Favorite createdFavorite = favoriteService.createFavorite(favoriteEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFavorite);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{favoriteId}")
    public ResponseEntity<Favorite> updateFavorite(@RequestBody Favorite favoriteEntity, @PathVariable Long favoriteId) {
        Favorite favorite = favoriteService.updateFavorite(favoriteEntity, favoriteId);
        if (favorite != null) {
            return ResponseEntity.ok(favorite);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long favoriteId) {
        try {
            favoriteService.deleteFavorite(favoriteId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

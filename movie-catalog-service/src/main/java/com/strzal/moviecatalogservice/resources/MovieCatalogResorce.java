package com.strzal.moviecatalogservice.resources;

import com.strzal.moviecatalogservice.models.CatalogItem;
import com.strzal.moviecatalogservice.models.Movie;
import com.strzal.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResorce {

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class );

        return ratings.getUserRating().stream().map(rating -> {

            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "first movie", rating.getRating());
        })
                .collect(Collectors.toList());

    }
}

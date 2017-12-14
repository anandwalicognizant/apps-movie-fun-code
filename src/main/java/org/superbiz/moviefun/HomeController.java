package org.superbiz.moviefun;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Map;

/**
 * Created by aw169 on 12/12/17.
 */

import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
   /* @Autowired
    @Qualifier("moviesTM")*/
    private final PlatformTransactionManager moviesTM;
  /*  @Autowired
    @Qualifier("albumsTM")*/
    private final PlatformTransactionManager albumsTM;

    //@Autowired
    public HomeController(MoviesBean moviesBean, AlbumsBean albumsBean, MovieFixtures movieFixtures, AlbumFixtures albumFixtures, PlatformTransactionManager moviesTM,  PlatformTransactionManager albumsTM) {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;

        this.moviesTM = moviesTM;
        this.albumsTM = albumsTM;
    }

    @GetMapping("/")
    public String index() {

        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {

        System.out.println("movies TM :" + moviesTM );
        System.out.println("albums TM :" + albumsTM);

        addMovies();

        System.out.println("In setup mapping movies");

        addAlbums();

        System.out.println("In setup mapping albums");
        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());

        return "setup";
    }

    private void addAlbums() {
        TransactionStatus transactionStatus = albumsTM.getTransaction(null);
        try {

            for (Album album : albumFixtures.load()) {
                albumsBean.addAlbum(album);
            }
        }catch(Exception e){
            albumsTM.rollback(transactionStatus);
            throw e;

        }
        albumsTM.commit(transactionStatus);
    }

    private void addMovies() {
        TransactionStatus transaction = moviesTM.getTransaction(null);
        try {

            for (Movie movie : movieFixtures.load()) {

                moviesBean.addMovie(movie);
            }
        }catch(Exception e){
            moviesTM.rollback(transaction);
            throw e;
        }
        moviesTM.commit(transaction);
    }
}

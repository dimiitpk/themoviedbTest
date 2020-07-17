package com.dimi.themoviedb.utils

class MovieGenres {

    companion object {

        private const val GENRE_POPULAR = 0 // this is not by api
        private const val GENRE_ACTION = 28
        private const val GENRE_ADVENTURE = 12
        private const val GENRE_ANIMATION = 16
        private const val GENRE_COMEDY = 35
        private const val GENRE_CRIME = 80
        private const val GENRE_DOCUMENTARY = 99
        private const val GENRE_DRAMA = 18
        private const val GENRE_FAMILY = 10751
        private const val GENRE_FANTASY = 14
        private const val GENRE_HISTORY = 36
        private const val GENRE_HORROR = 27
        private const val GENRE_MUSIC = 10402
        private const val GENRE_MYSTERY = 9648
        private const val GENRE_ROMANCE = 10749
        private const val GENRE_SCI_FI = 878
        private const val GENRE_TV_MOVIE = 10770
        private const val GENRE_THRILLER = 53
        private const val GENRE_WAR = 10752
        private const val GENRE_WESTERN = 37


        fun getAllGenreIds(): IntArray? {
            return intArrayOf(
                GENRE_POPULAR,
                GENRE_ACTION,
                GENRE_ADVENTURE,
                GENRE_ANIMATION,
                GENRE_COMEDY,
                GENRE_CRIME,
                GENRE_DOCUMENTARY,
                GENRE_DRAMA,
                GENRE_FAMILY,
                GENRE_FANTASY,
                GENRE_HISTORY,
                GENRE_HORROR,
                GENRE_MUSIC,
                GENRE_MYSTERY,
                GENRE_ROMANCE,
                GENRE_SCI_FI,
                GENRE_TV_MOVIE,
                GENRE_THRILLER,
                GENRE_WAR,
                GENRE_WESTERN
            )
        }
        
        fun getGenreIdByName(genreName: String?): Int {
            return when (genreName) {
                "Popular" -> GENRE_POPULAR
                "Adventure" -> GENRE_ADVENTURE
                "Animation" -> GENRE_ANIMATION
                "Comedy" -> GENRE_COMEDY
                "Crime" -> GENRE_CRIME
                "Documentary" -> GENRE_DOCUMENTARY
                "Drama" -> GENRE_DRAMA
                "Family" -> GENRE_FAMILY
                "Fantasy" -> GENRE_FANTASY
                "History" -> GENRE_HISTORY
                "Horror" -> GENRE_HORROR
                "Music" -> GENRE_MUSIC
                "Mystery" -> GENRE_MYSTERY
                "Romance" -> GENRE_ROMANCE
                "Sci-Fi" -> GENRE_SCI_FI
                "Thriller" -> GENRE_THRILLER
                "TV Movie" -> GENRE_TV_MOVIE
                "War" -> GENRE_WAR
                "Western" -> GENRE_WESTERN
                else -> GENRE_ACTION
            }
        }

        fun getGenreName( genreID: Int ): String? {
            var genreName = "Action"
            when (genreID) {
                GENRE_POPULAR -> {
                    genreName = "Popular"
                }
                GENRE_ACTION -> {
                    genreName = "Action"
                }
                GENRE_ADVENTURE -> {
                    genreName = "Adventure"
                }
                GENRE_ANIMATION -> {
                    genreName = "Animation"
                }
                GENRE_COMEDY -> {
                    genreName = "Comedy"
                }
                GENRE_CRIME -> {
                    genreName = "Crime"
                }
                GENRE_DOCUMENTARY -> {
                    genreName = "Documentary"
                }
                GENRE_DRAMA -> {
                    genreName = "Drama"
                }
                GENRE_FAMILY -> {
                    genreName = "Family"
                }
                GENRE_FANTASY -> {
                    genreName = "Fantasy"
                }
                GENRE_HISTORY -> {
                    genreName = "History"
                }
                GENRE_HORROR -> {
                    genreName = "Horro"
                }
                GENRE_MUSIC -> {
                    genreName = "Music"
                }
                GENRE_MYSTERY -> {
                    genreName = "Mystery"
                }
                GENRE_ROMANCE -> {
                    genreName = "Romance"
                }
                GENRE_SCI_FI -> {
                    genreName = "Sci-Fi"
                }
                GENRE_THRILLER -> {
                    genreName = "Thriller"
                }
                GENRE_TV_MOVIE -> {
                    genreName = "TV Movie"
                }
                GENRE_WAR -> {
                    genreName = "War"
                }
                GENRE_WESTERN -> {
                    genreName = "Western"
                }
            }
            return genreName
        }
    }
}
package com.dimi.themoviedb.utils

class TVShowGenres {

    companion object {

        private const val GENRE_POPULAR = 0 // this is not by api
        private const val GENRE_ACTION_AND_ADVENTURE = 10759
        private const val GENRE_ANIMATION = 16
        private const val GENRE_COMEDY = 35
        private const val GENRE_CRIME = 80
        private const val GENRE_DOCUMENTARY = 99
        private const val GENRE_DRAMA = 18
        private const val GENRE_FAMILY = 10751
        private const val GENRE_KIDS = 10762
        private const val GENRE_MYSTERY = 9648
        private const val GENRE_NEWS = 10763
        private const val GENRE_REALITY = 10764
        private const val GENRE_SCI_FI_AND_FANTASY = 10765
        private const val GENRE_SOAP = 10766
        private const val GENRE_TALK = 10767
        private const val GENRE_WAR_AND_POLITICS = 10768
        private const val GENRE_WESTERN = 37


        fun getAllGenreIds(): IntArray? {
            return intArrayOf(
                GENRE_POPULAR,
                GENRE_ACTION_AND_ADVENTURE,
                GENRE_ANIMATION,
                GENRE_COMEDY,
                GENRE_CRIME,
                GENRE_DOCUMENTARY,
                GENRE_DRAMA,
                GENRE_FAMILY,
                GENRE_KIDS,
                GENRE_MYSTERY,
                GENRE_NEWS,
                GENRE_REALITY,
                GENRE_SCI_FI_AND_FANTASY,
                GENRE_SOAP,
                GENRE_TALK,
                GENRE_WAR_AND_POLITICS,
                GENRE_WESTERN
            )
        }
        
        fun getGenreIdByName(genreName: String?): Int {
            return when (genreName) {
                "Popular" -> GENRE_POPULAR
                "Action & Adventure" -> GENRE_ACTION_AND_ADVENTURE
                "Animation" -> GENRE_ANIMATION
                "Comedy" -> GENRE_COMEDY
                "Crime" -> GENRE_CRIME
                "Documentary" -> GENRE_DOCUMENTARY
                "Drama" -> GENRE_DRAMA
                "Family" -> GENRE_FAMILY
                "Kids" -> GENRE_KIDS
                "Mystery" -> GENRE_MYSTERY
                "News" -> GENRE_NEWS
                "Reality" -> GENRE_REALITY
                "Sci-Fi & Fantasy" -> GENRE_SCI_FI_AND_FANTASY
                "Soap" -> GENRE_SOAP
                "Talk" -> GENRE_TALK
                "War & Politics" -> GENRE_WAR_AND_POLITICS
                "Western" -> GENRE_WESTERN
                else -> GENRE_ACTION_AND_ADVENTURE
            }
        }

        fun getGenreName( genreID: Int ): String? {
            var genreName = "Action"
            when (genreID) {
                GENRE_POPULAR -> {
                    genreName = "Popular"
                }
                GENRE_ACTION_AND_ADVENTURE -> {
                    genreName = "Action & Adventure"
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
                GENRE_KIDS -> {
                    genreName = "Kids"
                }
                GENRE_MYSTERY -> {
                    genreName = "Mystery"
                }
                GENRE_NEWS -> {
                    genreName = "News"
                }
                GENRE_REALITY -> {
                    genreName = "Reality"
                }
                GENRE_SCI_FI_AND_FANTASY -> {
                    genreName = "Sci-Fi & Fantasy"
                }
                GENRE_WAR_AND_POLITICS -> {
                    genreName = "War & Politics"
                }
                GENRE_SOAP -> {
                    genreName = "Soap"
                }
                GENRE_TALK -> {
                    genreName = "Talk"
                }
                GENRE_WESTERN -> {
                    genreName = "Western"
                }
            }
            return genreName
        }
    }
}
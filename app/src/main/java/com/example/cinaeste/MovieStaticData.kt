package com.example.cinaeste

fun getFavoriteMovies(): List<Movie> {
    return listOf(
        Movie(1,"Pride and prejudice",
            "Sparks fly when spirited Elizabeth Bennet meets single, rich, and proud Mr. Darcy. But Mr. Darcy reluctantly finds himself falling in love with a woman beneath his class. Can each overcome their own pride and prejudice?",
        "16.02.2005.","https://www.imdb.com/title/tt0414387/",
        "drama"),
        Movie(2, "Mad Max", "Max is a survivor who roams the wasteland and becomes entangled in various conflicts with gangs, warlords, and other survivors.",
        "15.05.2015", "https://www.imdb.com/title/tt1392190/",
        "action"),
        Movie(3, "The Hunger Games", "Capitol holds an annual televised competition known as the Hunger Games, in which a boy and a girl from each of the twelve districts are selected to fight to the death until only one survives.",
        "23.03.2012", "https://www.imdb.com/title/tt1392170/",
        "action"),
        Movie(4,"Pulp Fiction",
            "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
            "14.10.1994.","https://www.imdb.com/title/tt0110912/",
            "crime"),
        Movie(5, "Titanic",
        "A young woman falls in love with a wealthy man aboard the ill-fated Titanic ship.",
        "19.19.1997", "https://www.imdb.com/title/tt0120338/", "romance")

    )
}
fun getRecentMovies(): List<Movie> {
    return listOf(
        Movie(1,"Creed III",
            "Adonis has been thriving in both his career and family life, but when a c hildhood friend and former boxing prodigy resurfaces, the face-off is more than just a fight.",
            "03.03.2023.","https://www.imdb.com/title/tt11145118",
            "drama"),
        Movie(2, "Avatar: The Way of Water",
        "The story follow Jake Sully and Neytiri as they explore new regions of Pandora and encounter new threats and allies.",
            "16.12.2022", "https://www.imdb.com/title/tt9777644/",
            "science-fiction"),
        Movie(3, "Black Panther: Wakanda Forever",
        "The people of Wakanda fight to protect their home from intervening world powers as they mourn the death of King T'Challa.",
        "10.11.2022", "https://www.imdb.com/title/tt9113084/",
        "action")
    )
}

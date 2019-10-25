# Movie-Seeker-Application

A movie application using basic concepts for the coursework of the Mobile Development Module at Edinburgh Napier University. Movie Seeker lets the user show popular and
most rated movies, as well as creating a favourite list. It is mainly used with a network but maintains some offline functionalities because favourites movies are also stored locally
through a SQLite database. The data content is provided by TMDB which offers its API key by registering to their website.

Movie seeker has a clean and simple interface. It is divided into different activities, but I have decided for an easier user’s experience to use fragment activities inside containers. This
allows to process and show different parts of the app at the same time, without the necessity to click or to open new activities.

The main element of the fragment is a gridview contained in a scrollview. The gridview is made of poster’s image which are clickable to show movies’ information.
While the users interact with the main screen, some processes are done in the background using AsyncTask’s class extension, and Listener’s object and methods for catching user’s
inputs either on the gridview or on the settings. 

Finally, the app shows a different number of box images based on the screen’s size, in order to accommodate both tablets
and smartphones; each box maintains a light design with the use of Picasso to always re-size the images’ height by 1.5 of the width

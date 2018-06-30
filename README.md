# Project 2 - *Flixster*

**Flixster** shows the latest movies currently playing in theaters. The app utilizes the Movie Database API to display images and basic information about these movies to the user.

Time spent: **15** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **scroll through current movies** from the Movie Database API

The following **optional** features are implemented:

* [x] For each movie displayed, user can see the following details:
  * [x] Title, Poster Image, Overview (Portrait mode)
  * [x] Title, Backdrop Image, Overview (Landscape mode)
* [x] Display a nice default [placeholder graphic](https://guides.codepath.com/android/Displaying-Images-with-the-Glide-Library#advanced-usage) for each image during loading
* [x] Allow user to view details of the movie including ratings and popularity within a separate activity
* [x] Improved the user interface by experimenting with styling and coloring.
* [x] Apply rounded corners for the poster or background images using [Glide transformations](https://guides.codepath.com/android/Displaying-Images-with-the-Glide-Library#transformations)
* [x] Apply the popular [Butterknife annotation library](http://guides.codepath.com/android/Reducing-View-Boilerplate-with-Butterknife) to reduce boilerplate code.
* [x] Allow video trailers to be played in full-screen using the YouTubePlayerView from the details screen.

The following **additional** features are implemented:

* [x] Embed YouTube player right into movie details page instead of making it a separate activity to make for a smoother flow.
* [x] Store movie id as a field in the Movie class rather than fetching it anew every time.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

![Portrait Demo](stories_port.gif)
![Landscape Demo](stories_land.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes
Doing the stretch goals, especially wiring the YouTubePlayer, were a good way to test what we've learned beause they didn't have 
walkthrough videos. I had some trouble getting used to the asynchronous execution when waiting for my requests to return, but
once I got help it made more sense.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android
- [ButterKnife](http://jakewharton.github.io/butterknife/) - Field and method binding for Android views
- [YouTubeAndroidPlayer](https://developers.google.com/youtube/android/player/) - Incorporate video playback into Android applicartions

## License

    Copyright [2018] [Alex Goodman]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

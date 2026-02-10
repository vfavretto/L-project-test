
Itinerary builder app that allows the user mark interest points in the world map.

### Home Screen

A screen containing the all user's Trips in a list. A trip contains: Name, Image; and should be favoritable or bookmarkable

##### User interactions

* search and filter trips
* add new trip button
* clicks in a trip to navigate to the **Trip Screen**

### Trip Screen

A screen with bottom navigation bar that navigates the user to the main destinations

#### Home Destination (the main one)

A full screen map (using Google map API); Everything here is bonded to a Trip

##### User interactions

* Interest Mark: Click at any point of the map to create a Interest Mark; the Interest Mark represents a places where the user wants to go
  * when user clicks in a created Interest Mark, a card should open with the details of the location, containing
    * image
    * location name
    * optional date
    * ranking of interest
    * edit option (name and image would be retrieved by Google but can be overwritten by user)
    * delete option
    * location coordinates (user can open other map apps clicking in it)
* Distance toggle: A toggle button when activated, the map show a line between all marked points and the distance between the points
* Re-center: A button that re center the map view, zooming out until all marked points be visible

#### Destination List Destination

A list containing all interest mark cards and a top search bar

##### User interactions

* search and filter the cards
* click in a card to see its details (navigates to **Interest Mark Details Screen**)

#### Schedule List Destination

A list containing all interest mark cards that have scheduled option ordered by date. The schedule details should be highlighted in this card. The main purpose of this card is to alert the user of upcoming events and stuffs like that

##### User interactions

* search and filter the cards
* click in a card to see its details (navigates to **Interest Mark Details Screen**)

### Interest Mark Details Screen

A screen containing all details of the marked interest, such as name, image, schedule, tags, events, coordinates, user quick note etc.



## Technical Requirements

Android multi-module Clean Architecture with MVI and Jetpack Compose

MUST follows SOLID, DRY, Clean Architecture principles

##### Database

* Room
  * Relations should use edge graph strategy, like the Meta's TAO

##### Dependency Injection

* Koin

##### Navigation

* Jetpack Compose Navigation3 ([Navigation 3](https://developer.android.com/guide/navigation/navigation-3))

##### Theme

* Material3 based theme
* Greenish color palette
* Adventure and Freedom vibe
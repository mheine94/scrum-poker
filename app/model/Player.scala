package model

import model.PlayerStates.PlayerState

case class Player(val name: String, var choice: String, var state: PlayerState) {
}

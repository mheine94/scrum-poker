package model;

case class JoinTeamDto(playerName: String)
object JoinTeamDto {
  def unapply(dto: JoinTeamDto): Option[String] = Some(dto.playerName)
}


package model;

case class CreateTeamDto(teamName: String)
object CreateTeamDto {
def unapply(dto: CreateTeamDto): Option[String] = Some(dto.teamName)
}


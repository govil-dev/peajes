package co.quind.peajes.tollcollection.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("lanes")
public class LaneEntity {
    @Id
    private String laneId;
    private String stationId;
    private String status;

    public String getLaneId() { return laneId; }
    public void setLaneId(String laneId) { this.laneId = laneId; }
    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

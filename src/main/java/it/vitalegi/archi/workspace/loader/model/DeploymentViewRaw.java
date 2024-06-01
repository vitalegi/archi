package it.vitalegi.archi.workspace.loader.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeploymentViewRaw extends ViewRaw {
    String scope;
    String environment;
}
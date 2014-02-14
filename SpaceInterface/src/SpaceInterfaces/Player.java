package SpaceInterfaces;

/**
 *
 * @author Peter
 */
public interface Player {
    Float x = null;
    Float y = null;
    Long id = null;
    String name = null;
    Integer hp = null;
            
    public void SetX(Float x);
    public void SetY(Float y);
    public void SetId(Long id);
    public void SetName(String name);
    public void SetHp(Integer hp);    
}

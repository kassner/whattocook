package se.kassner.whattocook;

public class RecipeAssignedToSessionException extends RuntimeException
{
    public RecipeAssignedToSessionException()
    {
        super("The recipe is currently assigned to a session.");
    }
}

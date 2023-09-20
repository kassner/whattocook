package se.kassner.whattocook.manage;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.kassner.whattocook.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/manage/recipe")
public class RecipeController
{
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final SessionService sessionService;

    public RecipeController(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, SessionService sessionService)
    {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String get(Model model)
    {
        List<Recipe> recipes = recipeRepository.findAllForListing();
        model.addAttribute("recipes", recipes);

        return "recipe/index";
    }

    @GetMapping("/add")
    public String add(RecipeForm recipeForm, Model model)
    {
        model.addAttribute("form_title", "Add recipe");
        model.addAttribute("form_url", "/manage/recipe/add");

        return "recipe/form";
    }

    @PostMapping("/add")
    public String addPost(@Valid RecipeForm recipeForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model)
    {
        model.addAttribute("form_title", "Add recipe");
        model.addAttribute("form_url", "/manage/recipe/add");

        if (bindingResult.hasErrors()) {
            return "recipe/form";
        }

        Recipe recipe = new Recipe();
        recipe.setName(recipeForm.getName());
        recipe.setInstructions(recipeForm.getInstructions());
        recipe.setIngredients(convertIngredients(recipeForm.getIngredients()));

        return saveRecipe(recipe, model, redirectAttributes);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long recipeId, Model model, RedirectAttributes redirectAttributes)
    {
        Recipe recipe;
        try {
            recipe = recipeRepository.getReferenceById(recipeId);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manage/recipe";
        }

        RecipeForm recipeForm = new RecipeForm(recipe);
        model.addAttribute("form_title", String.format("Edit recipe \"%s\"", recipe.getName()));
        model.addAttribute("form_url", String.format("/manage/recipe/%d/edit", recipe.getId()));
        model.addAttribute("recipeForm", recipeForm);

        return "recipe/form";
    }

    @PostMapping(value = "/{id}/edit")
    public String edit(
        @PathVariable("id") Long recipeId,
        @Valid RecipeForm recipeForm,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    )
    {
        Recipe recipe;
        try {
            recipe = recipeRepository.getReferenceById(recipeId);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manage/recipe";
        }

        model.addAttribute("form_title", String.format("Edit recipe \"%s\"", recipe.getName()));
        model.addAttribute("form_url", String.format("/manage/recipe/%d/edit", recipe.getId()));
        model.addAttribute("recipeForm", recipeForm);

        if (bindingResult.hasErrors()) {
            return "recipe/form";
        }

        recipe.setName(recipeForm.getName());
        recipe.setInstructions(recipeForm.getInstructions());
        recipe.setIngredients(convertIngredients(recipeForm.getIngredients()));

        return saveRecipe(recipe, model, redirectAttributes);
    }

    private Set<Ingredient> convertIngredients(String[] ingredientNames)
    {
        Set<Ingredient> recipeIngredients = new HashSet<>();

        for (String ingredientName : ingredientNames) {
            if (ingredientName.isEmpty()) {
                continue;
            }

            Ingredient ingredient = ingredientRepository.getOneByName(ingredientName);

            if (ingredient == null) {
                ingredient = new Ingredient(ingredientName);
                ingredientRepository.save(ingredient);
            }

            recipeIngredients.add(ingredient);
        }

        return recipeIngredients;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes)
    {
        try {
            Recipe recipe = recipeRepository.getReferenceById(id);
            if (!canDelete(recipe)) {
                throw new Exception("The recipe cannot be deleted because it is assigned to a session.");
            }

            recipeRepository.delete(recipe);
            redirectAttributes.addFlashAttribute("success", String.format("Recipe %s deleted", recipe.getName()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/manage/recipe";
    }

    private String saveRecipe(Recipe recipe, Model model, RedirectAttributes redirectAttributes)
    {
        try {
            recipeRepository.saveAndFlush(recipe);
            redirectAttributes.addFlashAttribute("success", String.format("Recipe \"%s\" modified", recipe.getName()));
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException cause = (ConstraintViolationException) e.getCause();
            if (cause.getConstraintName().equals("recipe_name_unique")) {
                model.addAttribute("error", "There is a recipe with the chosen name is already registered");
            } else {
                model.addAttribute("error", e.getMessage());
            }

            return "recipe/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/manage/recipe";
    }

    private boolean canDelete(Recipe recipe)
    {
        Session session = sessionService.get();
        if (session == null) {
            return true;
        }

        return !(session.getRecipeId() == recipe.getId());
    }
}

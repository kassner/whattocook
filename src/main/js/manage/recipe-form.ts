const RecipeForm = {
    removeIngredient: (el : any) => {
        const ingredients = document.getElementById("ingredients");
        if (ingredients == null) {
            return;
        }

        // keep last element
        if (ingredients.children.length == 1) {
            RecipeForm.cleanInputs(ingredients);
        } else {
            el?.remove();
        }
    },

    addIngredient: () => {
        const ingredients = document.getElementById("ingredients");
        if (ingredients == null) {
            return;
        }

        const nextId = parseInt(ingredients.lastElementChild?.attributes.getNamedItem("data-id")?.value || "0") + 1;
        const template = ingredients.firstElementChild?.outerHTML || "";

        const parser = new DOMParser();
        const el : any = parser.parseFromString(template, 'text/html').body.firstChild;

        if (el == null) {
            return;
        }

        RecipeForm.cleanInputs(el);

        el.attributes.getNamedItem("data-id").value = nextId;
        el.querySelector("label").attributes.for.value = 'ingredient_' + nextId;
        el.querySelector("input[type=text]").attributes.id.value = 'ingredient_' + nextId;

        ingredients.appendChild(el);
    },

    cleanInputs: (el : any) => {
        if (el?.children != undefined) {
            [].forEach.call(el.children, function(child) {
                RecipeForm.cleanInputs(child);
            });
        }

        if (el.tagName.toLowerCase() == 'input') {
            el.value = '';
            el.attributes.value.value = '';
        }
    }
};

export default RecipeForm;

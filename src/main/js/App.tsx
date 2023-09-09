import { createRoot } from 'react-dom/client';
import { useState } from 'react';
import { Recipe, Ingredient } from './Model';
import './App.scss';

const IngredientEl = (props: {ingredient: Ingredient}) => {
    const ingredient = props.ingredient;

    const removeIngredient = (ingredient: Ingredient) => {
        console.log('@TODO removeIngredient');
        console.log(ingredient);
    };

    return (
        <li className="list-group-item" key={ingredient.id}>
            <span className="name">{ingredient.name}</span>
            <button className="remove" onClick={() => removeIngredient(ingredient)}>Remove</button>
        </li>
    )
}

const App = () => {
    const [recipe, setRecipe] = useState<Recipe|null>(null);

    const loadRecipe = (event: any) => {
        console.log("@TODO loadRecipe");
        const recipe : Recipe = {
            id: 123,
            name: 'Fish tacos',
            description: " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras viverra massa nec libero varius, et fermentum ex aliquam. Proin vitae arcu tortor. Sed sollicitudin et magna vitae tristique. Etiam non vestibulum sem. Quisque elementum, justo vel efficitur dapibus, mauris velit placerat metus, eu posuere nunc metus in nibh. Cras condimentum lectus diam, eget tristique nunc molestie in. Nullam nec arcu facilisis, pretium nisl in, cursus nisi. Aliquam lobortis quam enim, ac faucibus purus dignissim at. Praesent a dui id neque accumsan dapibus eget vel quam. Nunc at nunc interdum, pulvinar nisl vel, imperdiet neque. Nunc luctus massa vitae ligula lacinia iaculis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed mattis placerat est, eget congue nisi fermentum et. Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n        In bibendum urna sit amet orci pretium blandit. Etiam dui quam, scelerisque sed lacus et, porta venenatis dolor. Donec a enim mi. Etiam dictum orci eget est iaculis, et dictum ligula porta. In semper felis eget mauris maximus tempus. Maecenas lobortis pretium risus. Praesent a sollicitudin leo. Sed at cursus odio. In interdum nisi quis sem facilisis tristique. Pellentesque blandit mi a lorem accumsan, non dignissim dolor aliquet. Curabitur justo nisl, rutrum quis arcu nec, vulputate fermentum ligula. Vivamus et lectus at quam ornare suscipit ut vel lorem. Vestibulum suscipit pulvinar ornare. Integer varius, lorem ut varius ultricies, tortor urna ullamcorper urna, eget vulputate diam velit eu sapien. Aliquam laoreet nulla at massa posuere, ut varius odio volutpat.",
            ingredients: [
                {id: 1, name: "Fish"} as Ingredient,
                {id: 2, name: "Tortilla"} as Ingredient,
            ],
        };

        setRecipe(recipe);
    };

    if (!recipe) {
        return (
            <div className="container h-100">
                <div className="row mt-20 justify-content-center align-items-center">
                    <div className="p-5 bg-body-tertiary rounded-3 text-center">
                        <h1 className="display-5">What to cook today?</h1>
                        <button className="btn btn-primary btn-lg mt-5" type="button" onClick={loadRecipe}>Give me a recipe</button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="container h-100">
            <div className="row h-100 pt-2" id="recipe">
                <div className="col col-2 h-100">
                    <p>@TODO session history</p>
                </div>
                <div className="col col-10">
                    <div className="container card pb-4">
                        <h1 className="card-title">{recipe.name}</h1>
                        <div className="row">
                            <div className="col col-4">
                                <div className="card m-1 p-1 h-100">
                                    <h2 className="card-title">Ingredients</h2>
                                    <div className="card-body">
                                        <ul className="list-group">
                                            {recipe.ingredients.map((element : Ingredient) : any => {
                                                return <IngredientEl ingredient={element} />;
                                            })}
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div className="col col-8">
                                <div className="card m-1 p-1 h-100">
                                    <h2 className="card-title">Instructions</h2>
                                    <div className="card-body">{nl2p(recipe.description)}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

const nl2p = (text: string) => {
    return text.split("\n").map(function(item, key) {
        // @TODO remove non-printable characters, i.e.: \r
        if (item.length == 0) {
            return;
        }

        return (<p>{item}</p>);
    });
};

const root = createRoot(document.body as HTMLElement);
root.render(<App />);

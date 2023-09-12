import { createRoot } from 'react-dom/client';
import { useState } from 'react';
import { Recipe, Ingredient, Event } from './Model';
import './App.scss';
import TimeAgo from 'react-timeago';

const IngredientEl = (props: {ingredient: Ingredient, loadRecipeCallback: CallableFunction}) => {
    const { ingredient, loadRecipeCallback } = props;

    const removeIngredient = (ingredient: Ingredient) => {
        fetch('/session/ingredient/' + ingredient.id, {method: 'DELETE'})
            .then(response => response.json())
            .then((data) => {
                if (data !== true) {
                    alert('Error: ' + data.message);
                    return;
                }

                loadRecipeCallback();
            })
            .catch((e) => {
                console.log(e);
                alert('error' + e);
            })
        ;
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
    const [history, setHistory] = useState<Event[]>([]);

    const loadRecipeClick = () => {
        fetch('/session', {method: 'POST'})
            .then(() => {
                loadRecipe();
                loadHistory();
            })
            .catch((e) => {
                console.log(e);
                alert('/session error: ' + e);
            })
        ;
    };

    const loadRecipe = () => {
        fetch('/session/recipe')
            .then(response => response.json())
            .then((data) => {
                const recipe : Recipe = data;
                setRecipe(recipe);
            })
            .catch((e) => {
                console.log(e);
                alert('/session/recipe error: ' + e);
            })
        ;
    };

    const loadHistory = () => {
        fetch('/session/history')
            .then(response => response.json())
            .then((data) => {
                const history : Event[] = data;
                setHistory(history);
            })
            .catch((e) => {
                console.log(e);
                alert('/session/history error: ' + e);
            })
        ;
    };

    if (!recipe) {
        return (
            <div className="container h-100">
                <div className="row mt-20 justify-content-center align-items-center">
                    <div className="p-5 bg-body-tertiary rounded-3 text-center">
                        <h1 className="display-5">What to cook today?</h1>
                        <button className="btn btn-primary btn-lg mt-5" type="button" onClick={loadRecipeClick}>Give me a recipe</button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="container h-100">
            <div className="row h-100 pt-2">
                <div className="col col-2 h-100">
                    <ul className="list-group" id="session-history">
                        <li className="list-group-item">Session history</li>
                        {history.map((element : Event) : any => {
                            return <li className="list-group-item p-1">
                                <p className="type">{element.type}</p>
                                {element.description && <p className="description">{element.description}</p>}
                                <TimeAgo date={element.date} live={true} minPeriod={10} />
                            </li>
                        })}
                    </ul>
                </div>
                <div className="col col-10">
                    <div className="container card pb-4" id="recipe">
                        <h1 className="card-title">{recipe.name}</h1>
                        <div className="row">
                            <div className="col col-4">
                                <div className="card m-1 p-1 h-100">
                                    <h2 className="card-title">Ingredients</h2>
                                    <div className="card-body">
                                        <ul className="list-group">
                                            {recipe.ingredients.map((element : Ingredient) : any => {
                                                return <IngredientEl key={element.id} ingredient={element} loadRecipeCallback={() => {
                                                    loadRecipe();
                                                    loadHistory();
                                                }} />;
                                            })}
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div className="col col-8">
                                <div className="card m-1 p-1 h-100">
                                    <h2 className="card-title">Instructions</h2>
                                    <div className="card-body">{nl2p(recipe.instructions)}</div>
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

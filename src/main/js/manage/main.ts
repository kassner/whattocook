import './main.scss';
import Combobox from '@kassner/combobox';

if (document.getElementById('ingredients-container') != undefined) {
    new Combobox('#ingredient-add', '#ingredients-list', '#ingredients-selected');
}

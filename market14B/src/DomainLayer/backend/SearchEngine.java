package DomainLayer.backend;

import com.sun.source.tree.*;

public interface SearchEngine {
    Tree tree = new BinaryTree() {
        @Override
        public ExpressionTree getLeftOperand() {
            return null;
        }

        @Override
        public ExpressionTree getRightOperand() {
            return null;
        }

        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
            return null;
        }

    }
}

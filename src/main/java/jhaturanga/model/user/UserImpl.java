package jhaturanga.model.user;

import java.util.Objects;
import java.util.Optional;

/**
 * 
 * Implementation of {@link User}.
 *
 */
public final class UserImpl implements User {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String hashedPassword;
    private int winCount;
    private int drawCount;
    private int lostCount;

    /**
     * Construct a User.
     * 
     * @param username
     * @param hashedPassword
     * @param winCount
     * @param drawCount
     * @param lostCount
     */
    public UserImpl(final String username, final String hashedPassword, final int winCount, final int drawCount,
            final int lostCount) {
        this.username = Objects.requireNonNull(username);
        this.hashedPassword = hashedPassword;
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.lostCount = lostCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getHashedPassword() {
        return Optional.ofNullable(this.hashedPassword);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWinCount() {
        return this.winCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLostCount() {
        return this.lostCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDrawCount() {
        return this.drawCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayedMatchCount() {
        return this.winCount + this.drawCount + this.lostCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseWinCount() {
        this.winCount++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseDrawCount() {
        this.drawCount++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseLostCount() {
        this.lostCount++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserImpl other = (UserImpl) obj;
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

    /**
     * @return the string representation of this UserImpl.
     */
    public String toString() {
        return "UserImpl [username=" + username + ", hashedPassword=" + hashedPassword + ", winCount=" + winCount
                + ", drawCount=" + drawCount + ", lostCount=" + lostCount + "]";
    }

}

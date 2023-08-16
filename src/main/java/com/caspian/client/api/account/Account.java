package com.caspian.client.api.account;

import com.caspian.client.Caspian;
import com.caspian.client.api.account.microsoft.MicrosoftAuthException;
import com.caspian.client.api.config.Config;
import com.caspian.client.api.config.ConfigContainer;
import com.caspian.client.api.config.setting.StringConfig;
import com.caspian.client.api.manager.AccountManager;
import com.caspian.client.mixin.accessor.AccessorMinecraftClient;
import com.caspian.client.mixin.accessor.AccessorPlayerSkinProvider;
import com.caspian.client.util.Globals;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.report.AbuseReportContext;
import net.minecraft.client.report.ReporterEnvironment;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.ProfileKeys;
import net.minecraft.client.util.Session;
import net.minecraft.network.encryption.SignatureVerifier;

import java.io.IOException;
import java.net.Proxy;
import java.util.Optional;
import java.util.UUID;

/**
 *
 *
 * @author linus
 * @since 1.0
 *
 * @see AccountType
 * @see com.caspian.client.api.account.microsoft.MicrosoftAuthenticator
 */
public class Account extends ConfigContainer implements Globals
{
    //
    Config<String> password = new StringConfig("Password", "Password login " +
            "field of the account.", "");
    //
    private final AccountType type;
    private Session session;

    /**
     *
     *
     * @param type
     * @param username
     * @param password
     */
    public Account(AccountType type, String username, String password)
    {
        super(username);
        this.type = type;
        this.password.setValue(password);
    }

    /**
     *
     */
    public void login()
    {
        switch (type)
        {
            case MICROSOFT -> Caspian.EXECUTOR.execute(() ->
            {
                try
                {
                    setSession(AccountManager.MICROSOFT_AUTH.login(
                            getName(), password.getValue()));
                    Caspian.info("Logged into MSA account {} named {}", getName(), session.getUsername());
                } catch (MicrosoftAuthException | IOException e)
                {
                    Caspian.error("Failed to login to account {}", getName());
                    e.printStackTrace();
                }
            });
            case MOJANG ->
            {
                YggdrasilAuthenticationService authService =
                        new YggdrasilAuthenticationService(Proxy.NO_PROXY);
                YggdrasilUserAuthentication auth =
                        (YggdrasilUserAuthentication) authService.createUserAuthentication(Agent.MINECRAFT);
                auth.setUsername(getName());
                auth.setPassword(password.getValue());
                if (auth.canLogIn())
                {
                    try
                    {
                        auth.logIn();
                        GameProfile profile = auth.getSelectedProfile();
                        setSession(new Session(profile.getName(),
                                profile.getId().toString(),
                                auth.getAuthenticatedToken(), Optional.empty(),
                                Optional.empty(), Session.AccountType.MOJANG));
                    }
                    catch (AuthenticationException e)
                    {
                        Caspian.error("Failed to authenticate");
                        e.printStackTrace();
                    }
                }
                MinecraftSessionService sessionService =
                        authService.createMinecraftSessionService();
                ((AccessorMinecraftClient) mc).setAuthenticationService(authService);
                ((AccessorMinecraftClient) mc).setServicesSignatureVerifier(
                        SignatureVerifier.create(authService.getServicesKey()));
                ((AccessorMinecraftClient) mc).setSessionService(sessionService);
                ((AccessorMinecraftClient) mc).setSkinProvider(new PlayerSkinProvider(mc.getTextureManager(),
                        ((AccessorPlayerSkinProvider) mc.getSkinProvider()).getSkinCacheDir(),
                        sessionService));
            }
            case CRACKED -> setSession(new Session(getName(),
                    UUID.randomUUID().toString(), "",  Optional.empty(),
                    Optional.empty(), Session.AccountType.LEGACY));
        }
    }
    protected void xboxLiveLogin()
    {

    }
    protected void xstsLogin()
    {

    }

    /**
     *
     * @return
     */
    public String getPassword()
    {
        return password.getValue();
    }

    /**
     *
     *
     * @return
     */
    public AccountType getType()
    {
        return type;
    }

    /**
     *
     *
     * @return
     */
    public Session getSession()
    {
        return session;
    }

    /**
     *
     *
     * @param session
     */
    public void setSession(Session session)
    {
        this.session = session;
        ((AccessorMinecraftClient) mc).setSession(session);
        mc.getSessionProperties().clear();
        UserApiService apiService;
        try
        {
            apiService = ((AccessorMinecraftClient) mc).getAuthenticationService()
                    .createUserApiService(session.getAccessToken());
        }
        catch (AuthenticationException e)
        {
            Caspian.error("Authentication api service is offline");
            apiService = UserApiService.OFFLINE;
        }
        ((AccessorMinecraftClient) mc).setUserApiService(apiService);
        ((AccessorMinecraftClient) mc).setSocialInteractionsManager(new SocialInteractionsManager(mc,
                apiService));
        ((AccessorMinecraftClient) mc).setProfileKeys(ProfileKeys.create(apiService,
                session, mc.runDirectory.toPath()));
        ((AccessorMinecraftClient) mc).setAbuseReportContext(AbuseReportContext.create(
                ReporterEnvironment.ofIntegratedServer(), apiService));
    }
}

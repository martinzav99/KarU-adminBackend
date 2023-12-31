package com.ungspp1.gadminbackend.api.password;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ungspp1.gadminbackend.api.password.to.NewPassRequest;
import com.ungspp1.gadminbackend.api.password.to.RecoverPassRequestTO;
import com.ungspp1.gadminbackend.api.password.to.TokenRequestTO;
import com.ungspp1.gadminbackend.exceptions.EngineException;
import com.ungspp1.gadminbackend.model.entity.UserDE;
import com.ungspp1.gadminbackend.model.entity.UserResetPassDE;
import com.ungspp1.gadminbackend.model.repository.UserRepository;
import com.ungspp1.gadminbackend.model.repository.UserResetPassRepository;
import com.ungspp1.gadminbackend.service.SendMailService;

@Service
public class RecoverPassService {
    
    @Autowired
    private UserResetPassRepository userResetPassRepository;

    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private SendMailService mailFacade;

    public HttpStatus verifyEmail(RecoverPassRequestTO request) throws EngineException{

        Optional<UserDE> user = userRepository.findByEmail(request.getEmail());

        if(!user.isPresent())
            throw new EngineException("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        
        Optional<UserResetPassDE> token = userResetPassRepository.findByusername(user.get().getUsername());

        String code = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        
        if(!token.isPresent()){
            
            UserResetPassDE tokenNuevo = new  UserResetPassDE();
            tokenNuevo.setToken(code);
            tokenNuevo.setCodeExpirationDate(LocalDateTime.now().plusHours(3));
            tokenNuevo.setUserData(user.get());

            userResetPassRepository.save(tokenNuevo);
            mailFacade.sendTokenMail(user.get().getUsername(),request.getEmail(), code);
            return HttpStatus.OK;
        }
        else{ 
            UserResetPassDE tokenFounded = token.get();
            if (recentSend(tokenFounded))
                throw new EngineException("Un token fue enviado recientemente, por favor revisa tu email.", HttpStatus.BAD_REQUEST); 
            tokenFounded.setToken(code);
            tokenFounded.setCodeExpirationDate(LocalDateTime.now().plusHours(3));
            userResetPassRepository.save(tokenFounded);
            mailFacade.sendTokenMail(user.get().getUsername(),request.getEmail(), code);
            return HttpStatus.OK;
        }     
    }

    public boolean recentSend(UserResetPassDE token){
        LocalDateTime validDate = token.getCodeExpirationDate().minusHours(2).minusMinutes(45);
        return LocalDateTime.now().isBefore(validDate);
    }

    public HttpStatus verifyToken(TokenRequestTO request) throws EngineException{
        Optional<UserResetPassDE> tokenFound = userResetPassRepository.findByToken(request.getToken());
        
        if(!tokenFound.isPresent())
            throw new EngineException("Token no encontrado", HttpStatus.BAD_REQUEST);

        if (!tokenFound.get().getCodeExpirationDate().isAfter(LocalDateTime.now()))
            throw new EngineException("Token expirado", HttpStatus.BAD_REQUEST);
       
        return HttpStatus.OK;
    }

    public HttpStatus uploadNewPass(NewPassRequest request) throws EngineException{
        Optional<UserDE> user =userRepository.findByEmail(request.getEmail());

        if (!user.isPresent())
            throw new EngineException("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        
        UserDE userFounded = user.get();

        if (request.getPassword() == null)
            throw new EngineException("Por favor, complete los campos", HttpStatus.BAD_REQUEST); 

        if (sameOldPass(request, userFounded))
            throw new EngineException("Por favor, ingrese una contraseña distinta a la anterior", HttpStatus.BAD_REQUEST);
        
        if(!validPass(request))
            throw new EngineException("Por favor, ingrese una contraseña valida", HttpStatus.BAD_REQUEST);
        
        Optional<UserResetPassDE> token =userResetPassRepository.findByusername(userFounded.getUsername());

        if(!token.isPresent())
            throw new EngineException("Token no encontrado", HttpStatus.BAD_REQUEST);

        UserResetPassDE tokenFounded = token.get();
        tokenFounded.setCodeExpirationDate(LocalDateTime.now());
        userResetPassRepository.save(tokenFounded);

        userFounded.setPassword(request.getPassword());
        userRepository.save(userFounded);
        return HttpStatus.OK;                      
    }

    private boolean sameOldPass(NewPassRequest request ,UserDE user){
        return request.getPassword().equals(user.getPassword());
    }

    private boolean validPass(NewPassRequest request){
    
        String password = request.getPassword();

        if (8 <= password.length() && password.length()<=20){
            
            boolean hasMayus= false, hasMin= false, hasNumb= false, hasEspecial = false;

            for (int i = 0; i< password.length(); i++){
                if (ascciRange(password, i, 97, 122))
                    hasMin = true;
                    
                if (ascciRange(password, i, 65, 90))
                    hasMayus = true;
                    
                if (ascciRange(password, i, 48, 57))
                    hasNumb = true;
                    
                if (ascciRange(password,i, 33, 47) || ascciRange(password,i, 58, 64) || ascciRange(password,i, 91, 96) || ascciRange(password,i, 123, 254))
                    hasEspecial = true;          
            }
            return hasMayus && hasMin && hasNumb && hasEspecial;
        }
        else {
            return false;
        }
    }

    private boolean ascciRange(String word, int pos, int a , int b ){
        return a <= word.charAt(pos) && word.charAt(pos) <= b;
    }
}
